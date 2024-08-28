package nl.kubebit.core.usecases.release.util;

import nl.kubebit.core.usecases.common.util.HelmBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.exception.ReleaseNotUpdatedException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.util.HelmValuesMerger;

import static nl.kubebit.core.usecases.common.vars.GlobalVars.MANIFESTS_LOCATION;
import static nl.kubebit.core.usecases.common.vars.GlobalVars.YAML_EXT;

/**
 *
 */
@Component
@Validated
public class ManifestAsyncInstaller {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ReleaseGateway releaseGateway;
    private final TemplateGateway templateGateway;
    private final ManifestGateway manifestGateway;

    /**
     * Constructor
     * @param releaseGateway release gateway
     * @param templateGateway template gateway
     * @param manifestGateway manifest gateway
     */
    public ManifestAsyncInstaller(ReleaseGateway releaseGateway, TemplateGateway templateGateway, ManifestGateway manifestGateway) {
        this.releaseGateway = releaseGateway;
        this.templateGateway = templateGateway;
        this.manifestGateway = manifestGateway;
    }

    /**
     * Execute the installation of a template
     * @param project the project
     * @param namespace the namespace
     * @param template the template
     * @param release the release
     */
    @Async
    public void execute(Project project, Namespace namespace, Template template, Release release) {
        log.info("{} -> installing template: {}", release.namespaceId(), template.id());
        try {
            // set pending status
            releaseGateway.patch(release).orElseThrow(ReleaseNotUpdatedException::new);

            // get chart file
            var chart = templateGateway.getChart(template.id()).orElseThrow(() -> new TemplateNotFoundException(template.id()));

            //
            var manifestPath = Paths.get(MANIFESTS_LOCATION, project.id(), namespace.name(), release.id());
            var manifestFile = Paths.get(manifestPath.toString(), release.version() + YAML_EXT).toFile();
            Files.createDirectories(manifestPath);
            log.trace("manifest file: {}", manifestFile);

            // create values file for release
            var valuesFile = createValuesFile(template, release);

            // create helm template
            var process = HelmBuilder.init()
                    .template(release.id(), chart)
                    .namespace(release.namespaceId())
                    .values(valuesFile)
                    .executeAs();

            // create manifest
            try (var inputStream = process.getInputStream()) {
                manifestGateway.createManifest(inputStream, project.id(), release.id(), release.version().toString(), manifestFile);
            }
            process.waitFor();

            // apply manifest
            var resources = manifestGateway.applyManifest(manifestFile);
            if(resources == null || resources.isEmpty()) {
                throw new RuntimeException("no resources found in manifest");
            }

            // clean up old revisions
            cleanRevisions(manifestPath, release);

            // set deployed status
            releaseGateway.patch(release.setDeployedAndResources(resources)).orElseThrow(ReleaseNotUpdatedException::new);

        } catch (Exception e) {
            log.warn("{} -> error installing template: {}", namespace.id(), template.id(), e);
            if(releaseGateway.patch(release.setStatus(ReleaseStatus.FAILED, e.getMessage())).isEmpty()) {
                log.error("--> failed to update release status");
            }
        }
        log.trace("thread finished");
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Create a values file for the release
     * @param template the template
     * @param release the release
     * @return the values file
     * @throws IOException if an I/O error occurs
     */
    private Path createValuesFile(Template template, Release release) throws IOException {
        var valuesFile = Files.createTempFile(release.id() + "-", "-values.yaml");
        // todo: overwrite overlay values -> prefixed variables to dynamic values (like: $PROJECT_ID to project.id())
        HelmValuesMerger.execute(template.chartValues(), template.overlayValues(), release.values(), valuesFile);
        return valuesFile;
    }

    /**
     *  Clean up old revisions of the release
     * @param manifestPath path to manifest location
     * @param release release
     */
    private void cleanRevisions(Path manifestPath, Release release) {
        try {
            // get revisions and current release
            var filesToKeep = release.revisions().stream().map(revision -> revision.version() + YAML_EXT).collect(Collectors.toList());
            filesToKeep.add(release.version() + YAML_EXT);
            log.trace("files to keep: {}", filesToKeep);

            // iterate through the list of files and delete the ones that are not in the list
            for (Path file : Files.list(manifestPath).toList()) {
                if(!filesToKeep.contains(file.getFileName().toString())) {
                    log.trace("deleting file: {}", file);
                    Files.delete(file);
                }
            }

        } catch (Exception e) {
            log.warn("failed to clear revisions: {}", e.getMessage());
        }
    }

}
