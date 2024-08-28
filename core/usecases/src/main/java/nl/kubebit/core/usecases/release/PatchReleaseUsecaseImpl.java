package nl.kubebit.core.usecases.release;

import nl.kubebit.core.usecases.release.util.ManifestAsyncPatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.exception.ReleaseIsRunningException;
import nl.kubebit.core.entities.release.exception.ReleaseNotFoundException;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

@Usecase
class PatchReleaseUsecaseImpl implements PatchReleaseUsecase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;
    private final ReleaseGateway releaseGateway;
    private final ManifestAsyncPatcher manifestPatcher;

    /**
     * Constructor
     * @param projectGateway project gateway
     * @param namespaceGateway namespace gateway
     * @param releaseGateway release gateway
     * @param manifestPatcher manifest patcher
     */
    public PatchReleaseUsecaseImpl(
            ProjectGateway projectGateway,
            NamespaceGateway namespaceGateway,
            ReleaseGateway releaseGateway,
            ManifestAsyncPatcher manifestPatcher) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.releaseGateway = releaseGateway;
        this.manifestPatcher = manifestPatcher;
    }

    /**
     * 
     */
    @Override
    public ReleaseResponse execute(String projectId, String namespaceName, String releaseId) {
        log.info("{} - {} -> patch release: {}", projectId, namespaceName, releaseId);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var namespace = namespaceGateway.findByName(project, namespaceName).orElseThrow(() -> new NamespaceNotFoundException(namespaceName));
        var release = releaseGateway.findById(namespace.id(), releaseId).orElseThrow(() -> new ReleaseNotFoundException(releaseId));
        
        // check if release is running
        if(ReleaseStatus.isRunning(release.status())) {
            throw new ReleaseIsRunningException(release.id());
        }

        // update release
        var entity = new Release(
            release.id(), 
            release.version(),
            release.template(),
            release.values(),
            null,
            ReleaseStatus.PENDING_PATCH,
            null,
            null,
            release.revisions(),
            namespace.id());

        // patch manifest
        manifestPatcher.execute(project, namespace, entity);

        // return response
        return new ReleaseResponse(entity);
    }
    
}
