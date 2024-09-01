package nl.kubebit.core.usecases.release.util;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.exception.ReleaseNotUpdatedException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.resource.gateway.ResourceGateway;
import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
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
import java.util.stream.Collectors;

import static nl.kubebit.core.usecases.common.vars.GlobalVars.MANIFESTS_LOCATION;
import static nl.kubebit.core.usecases.common.vars.GlobalVars.YAML_EXT;

/**
 *
 */
@Component
@Validated
public class ManifestAsyncIRemover {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ReleaseGateway releaseGateway;
    private final ResourceGateway resourceGateway;

    /**
     * Constructor
     *
     * @param releaseGateway  release gateway
     * @param resourceGateway resource gateway
     */
    public ManifestAsyncIRemover(ReleaseGateway releaseGateway, ResourceGateway resourceGateway) {
        this.releaseGateway = releaseGateway;
        this.resourceGateway = resourceGateway;
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
        log.info("{} -> removing release: {}", release.namespaceId(), release.id());
        try {

            // set pending status
            releaseGateway.patch(release).orElseThrow(ReleaseNotUpdatedException::new);

            // delete resources
            release.resources().forEach(resource ->
                    resourceGateway.removeResource(namespace.id(), resource)
            );

            // delete revisions
            var manifestPath = Paths.get(MANIFESTS_LOCATION, project.id(), namespace.name(), release.id());
            deleteRevisions(manifestPath);

            //
            releaseGateway.patch(release.setStatus(ReleaseStatus.UNINSTALLED, null)).orElseThrow(ReleaseNotUpdatedException::new);

        } catch (Exception e) {
            log.warn("{} -> error deleting release: {}", namespace.id(), release.id(), e);
            if (releaseGateway.patch(release.setStatus(ReleaseStatus.FAILED, e.getMessage())).isEmpty()) {
                log.error("--> failed to update release status");
            }
        }
        log.trace("thread finished");
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Delete revisions of the release
     *
     * @param manifestPath path to manifest location
     */
    private void deleteRevisions(Path manifestPath) {
        try {
            if (!manifestPath.toFile().delete()) {
                log.warn("failed to delete manifest path: {}", manifestPath);
            }
        } catch (Exception e) {
            log.warn("failed to clear revisions: {}", e.getMessage());
        }
    }

}
