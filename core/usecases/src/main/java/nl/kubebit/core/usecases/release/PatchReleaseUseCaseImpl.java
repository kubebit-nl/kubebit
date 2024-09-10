package nl.kubebit.core.usecases.release;

import nl.kubebit.core.usecases.release.chore.ManifestAsyncChore;

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
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

@UseCase
class PatchReleaseUseCaseImpl implements PatchReleaseUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;
    private final ReleaseGateway releaseGateway;
    private final ManifestAsyncChore manifestPatcher;

    /**
     * Constructor
     * @param projectGateway project gateway
     * @param namespaceGateway namespace gateway
     * @param releaseGateway release gateway
     * @param manifestPatcher manifest patcher
     */
    public PatchReleaseUseCaseImpl(
            ProjectGateway projectGateway,
            NamespaceGateway namespaceGateway,
            ReleaseGateway releaseGateway,
            ManifestAsyncChore manifestPatcher) {
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
        var project = projectGateway.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        var namespace = namespaceGateway.findByName(project.id(), namespaceName).orElseThrow(NamespaceNotFoundException::new);
        var release = releaseGateway.findById(namespace.id(), releaseId).orElseThrow(ReleaseNotFoundException::new);
        
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
            release.icon(),
            ReleaseStatus.PENDING_PATCH,
            "",
            release.resources(),
            release.revisions(),
            namespace.id());

        // patch manifest
        manifestPatcher.execute(project, namespace, null, entity);

        // return response
        return new ReleaseResponse(entity);
    }
    
}
