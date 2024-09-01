package nl.kubebit.core.usecases.release.util;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.exception.ReleaseNotUpdatedException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.usecases.common.util.HelmBuilder;
import nl.kubebit.core.usecases.common.util.HelmValuesMerger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static nl.kubebit.core.usecases.common.vars.GlobalVars.MANIFESTS_LOCATION;
import static nl.kubebit.core.usecases.common.vars.GlobalVars.YAML_EXT;

/**
 *
 */
@Component
@Validated
public class ManifestAsyncMigrator {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ReleaseGateway releaseGateway;
    private final ManifestGateway manifestGateway;

    /**
     * Constructor
     *
     * @param releaseGateway  release gateway
     * @param manifestGateway manifest gateway
     */
    public ManifestAsyncMigrator(ReleaseGateway releaseGateway, ManifestGateway manifestGateway) {
        this.releaseGateway = releaseGateway;
        this.manifestGateway = manifestGateway;
    }

    /**
     * Execute the installation of a template
     *
     * @param project   the project
     * @param namespace the namespace
     * @param release   the release
     */
    @Async
    public void execute(Project project, Namespace namespace, Release release) {
        log.info("{} -> removing release: {}", namespace.id(), release.id());
        try {

            // set pending status
            releaseGateway.patch(release).orElseThrow(ReleaseNotUpdatedException::new);

            // create manifest path
            var manifestPath = Paths.get(MANIFESTS_LOCATION, project.id(), namespace.name(), release.id());
            var manifestFile = Paths.get(manifestPath.toString(), release.version() + YAML_EXT).toFile();
            Files.createDirectories(manifestPath);
            log.trace("manifest file: {}", manifestFile);

            // create helm template
            var process = HelmBuilder.init()
                    .additionalArgs("get", "manifest", release.id())
                    .namespace(namespace.id())
                    .executeAs();

            // wait for the process to finish
            process.waitFor(60, TimeUnit.SECONDS);

            // check if the process was not successful
            if (process.exitValue() != 0) {
                try (var errorStream = process.getErrorStream()) {
                    var errors = HelmBuilder.fetchStream(errorStream);
                    throw new RuntimeException("error fetching manifest: " + errors);
                }
            }

            // create manifest
            try (var inputStream = process.getInputStream()) {
                manifestGateway.createManifest(inputStream, project.id(), release.id(), release.version().toString(), manifestFile);
            }

            // get resources
            var resources = manifestGateway.getResources(manifestFile);
            if (resources == null || resources.isEmpty()) {
                throw new RuntimeException("no resources found in manifest");
            }

            // set deployed status
            releaseGateway.patch(release.setDeployedAndResources(resources)).orElseThrow(ReleaseNotUpdatedException::new);

        } catch (Exception e) {
            log.warn("{} -> error migrate release: {}", namespace.id(), release.id(), e);
        }
        log.trace("thread finished");
    }

}
