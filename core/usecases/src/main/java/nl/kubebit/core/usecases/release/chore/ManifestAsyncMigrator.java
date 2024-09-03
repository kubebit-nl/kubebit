package nl.kubebit.core.usecases.release.chore;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.TemplateRef;
import nl.kubebit.core.entities.release.exception.ReleaseNotUpdatedException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.usecases.common.util.ChoreUtils;
import nl.kubebit.core.usecases.common.util.HelmBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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

            // create manifest locations
            var manifestPath = ChoreUtils.createManifestPath(project, namespace, release);
            var manifestFile = Paths.get(manifestPath.toString(), release.version() + YAML_EXT).toFile();

            log.trace("create manifest file");
            try (var inputStream = HelmBuilder.init().get(HelmBuilder.GetCommand.MANIFEST, release.id()).namespace(namespace.id()).execute()) {
                manifestGateway.createManifest(inputStream, project.id(), release.version(), manifestFile);
            }

            log.trace("getting resources...");
            var resources = manifestGateway.getResources(manifestFile);
            log.trace("resources: {}", resources);

            log.trace("getting template...");
            TemplateRef template;
            try (var inputStream = HelmBuilder.init().get(HelmBuilder.GetCommand.METADATA, release.id()).namespace(namespace.id()).execute()) {
                Map<String, Object> metadata = new Yaml().load(inputStream);
                template = new TemplateRef(metadata.get("CHART").toString(), metadata.get("VERSION").toString());
                log.trace("template: {}", template);
            }

            log.trace("getting values...");
            Map<String, Object> values;
            try (var inputStream = HelmBuilder.init().get(HelmBuilder.GetCommand.VALUES, release.id()).namespace(namespace.id()).execute()) {
                values = new Yaml().load(inputStream);
                values.remove("USER-SUPPLIED VALUES");
                log.trace("values: {}", values);
            }

            // create release
            var entity = new Release(
                    release.id(),
                    1L,
                    template,
                    values,
                    null,
                    ReleaseStatus.DEPLOYED,
                    null,
                    resources,
                    List.of(),
                    namespace.id());

            // set deployed status
            releaseGateway.patch(entity).orElseThrow(ReleaseNotUpdatedException::new);

        } catch (Exception e) {
            log.warn("{} -> error migrate release: {}", namespace.id(), release.id(), e);
            if (releaseGateway.patch(release.setStatus(ReleaseStatus.FAILED, e.getMessage())).isEmpty()) {
                log.error("--> failed to update release status");
            }
        }
        log.trace("thread finished");
    }

}
