package nl.kubebit.core.usecases.release.chore;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.TemplateRef;
import nl.kubebit.core.entities.release.exception.ReleaseNotUpdatedException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.util.ManifestUtils;
import nl.kubebit.core.usecases.common.util.HelmBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static nl.kubebit.core.entities.common.vars.GlobalVars.YAML_EXT;

/**
 *
 */
@Component
public class ManifestMigrator {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ReleaseGateway releaseGateway;
    private final ManifestGateway manifestGateway;
    private final TemplateGateway templateGateway;

    /**
     * Constructor
     *
     * @param releaseGateway  release gateway
     * @param manifestGateway manifest gateway
     */
    public ManifestMigrator(ReleaseGateway releaseGateway, ManifestGateway manifestGateway, TemplateGateway templateGateway) {
        this.releaseGateway = releaseGateway;
        this.manifestGateway = manifestGateway;
        this.templateGateway = templateGateway;
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
            var manifestPath = ManifestUtils.createManifestPath(project, namespace, release);
            var manifestFile = Paths.get(manifestPath.toString(), release.version() + YAML_EXT).toFile();

            // create manifest file
            log.trace("create manifest file");
            try (var inputStream = HelmBuilder.init().get(HelmBuilder.GetCommand.MANIFEST, release.id()).namespace(namespace.id()).execute()) {
                manifestGateway.createManifest(inputStream, project.id(), release.version(), manifestFile);
            }

            // get resources
            var resources = getResources(manifestFile);

            // get helm template
            var template = gettingTemplate(namespace, release);

            // get helm values
            var values = gettingValues(namespace, release);

            // create release
            var entity = new Release(
                    release.id(),
                    1L,
                    template.map(TemplateRef::new).orElse(null),
                    values,
                    template.map(Template::icon).orElse(null),
                    ReleaseStatus.DEPLOYED,
                    null,
                    resources,
                    List.of(),
                    namespace.id());

            // update release
            releaseGateway.update(entity).orElseThrow(ReleaseNotUpdatedException::new);

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


    // --------------------------------------------------------------------------------------------
    // private methods

    /**
     * Getting resources
     *
     * @param manifestFile manifest file
     * @return resources
     * @throws IOException if an error occurs
     */
    private List<ReleaseResourceRef> getResources(File manifestFile) throws IOException {
        log.trace("getting resources...");
        return manifestGateway.getResources(manifestFile);
    }

    /**
     * Getting template
     *
     * @param namespace namespace
     * @param release   release
     * @return template
     * @throws IOException if an error occurs
     */
    private Optional<Template> gettingTemplate(Namespace namespace, Release release) throws IOException {
        log.trace("getting template...");
        String templateId = "";
        try (var inputStream = HelmBuilder.init().get(HelmBuilder.GetCommand.METADATA, release.id()).namespace(namespace.id()).execute()) {
            Map<String, Object> metadata = new Yaml().load(inputStream);
            templateId = metadata.get("CHART").toString() + "-" + metadata.get("VERSION").toString();
        }
        return templateGateway.findById(templateId);
    }

    /**
     * Getting values
     *
     * @param namespace namespace
     * @param release   release
     * @return values
     * @throws IOException if an error occurs
     */
    private Map<String, Object> gettingValues(Namespace namespace, Release release) throws IOException {
        log.trace("getting values...");
        Map<String, Object> values;
        try (var inputStream = HelmBuilder.init().get(HelmBuilder.GetCommand.VALUES, release.id()).namespace(namespace.id()).execute()) {
            values = new Yaml().load(inputStream);
            values.remove("USER-SUPPLIED VALUES");
        }
        return values;
    }

}
