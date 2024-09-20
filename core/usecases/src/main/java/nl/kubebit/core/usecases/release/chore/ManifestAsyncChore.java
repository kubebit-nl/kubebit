package nl.kubebit.core.usecases.release.chore;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.release.Manifest;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.exception.ReleaseNotUpdatedException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.resource.gateway.ResourceGateway;
import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.event.ServerSendEventGateway;
import nl.kubebit.core.usecases.release.chore.actions.ManifestActionApply;
import nl.kubebit.core.usecases.release.chore.actions.ManifestActionCreate;
import nl.kubebit.core.usecases.release.chore.actions.ManifestActionDelete;
import nl.kubebit.core.usecases.release.chore.properties.DynamicProperties;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@EnableConfigurationProperties(DynamicProperties.class)
public class ManifestAsyncChore {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ReleaseGateway releaseGateway;
    private final ManifestGateway manifestGateway;
    private final TemplateGateway templateGateway;
    private final ResourceGateway resourceGateway;
    private final ServerSendEventGateway eventGateway;

    //
    private final DynamicProperties properties;

    /**
     * Constructor
     *
     * @param releaseGateway  release gateway
     * @param manifestGateway manifest gateway
     * @param templateGateway template gateway
     * @param eventGateway    event gateway
     */
    public ManifestAsyncChore(ReleaseGateway releaseGateway, ManifestGateway manifestGateway, TemplateGateway templateGateway, ResourceGateway resourceGateway, ServerSendEventGateway eventGateway, DynamicProperties properties) {
        this.releaseGateway = releaseGateway;
        this.manifestGateway = manifestGateway;
        this.templateGateway = templateGateway;
        this.resourceGateway = resourceGateway;
        this.eventGateway = eventGateway;
        this.properties = properties;
    }

    /**
     *
     */
    @Async
    public void execute(Project project, Namespace namespace, Template template, Release release) {
        ReleaseResponse response = null;
        try {

            // set pending status
            releaseGateway.patch(release).orElseThrow(ReleaseNotUpdatedException::new);

            // create manifest object
            var manifest = new Manifest(project, namespace, template, release);

            // action: create manifest
            if (ManifestActionCreate.statusAllowed().contains(release.status())) {
                try(var action = new ManifestActionCreate(templateGateway, manifestGateway, properties.properties())) {
                    action.execute(manifest);
                }
            }

            // action: apply manifest
            if (ManifestActionApply.statusAllowed().contains(release.status())) {
                try(var action = new ManifestActionApply(manifestGateway)) {
                    action.execute(manifest);
                }
            }

            // action: delete resources
            if (ManifestActionDelete.statusAllowed().contains(release.status())) {
                try(var action = new ManifestActionDelete(resourceGateway)) {
                    action.execute(manifest);
                }
            }

            // get resources from manifest
            var resouces = release.status().equals(ReleaseStatus.UNINSTALLING) ? null : manifestGateway.getResources(manifest.getFile());

            //
            var entity = new Release(
                    release.id(),
                    release.version(),
                    release.template(),
                    release.values(),
                    release.icon(),
                    release.status().equals(ReleaseStatus.UNINSTALLING) ? ReleaseStatus.UNINSTALLED : ReleaseStatus.DEPLOYED,
                    "",
                    resouces,
                    release.revisions(),
                    release.namespaceId());

            // check if uninstalling
            if (release.status().equals(ReleaseStatus.UNINSTALLING)) {

                // delete release
                releaseGateway.delete(entity);
                response = new ReleaseResponse(entity);

            } else {

                // set status
                response = releaseGateway.patch(entity).map(ReleaseResponse::new).orElseThrow(ReleaseNotUpdatedException::new);
            }

        } catch (Exception e) {
            log.warn("failed chore: {}", e.getMessage());
            response = releaseGateway.patch(release.setStatus(ReleaseStatus.FAILED, e.getMessage())).map(ReleaseResponse::new).orElse(null);
            if (response == null) {
                log.error("failed to update release: {}", e.getMessage());
            }
        }

        // send event
        if (response != null) {
            eventGateway.sendEvent(project.id(), response);
        }

        //
        log.trace("thread finished");
    }

}
