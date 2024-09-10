package nl.kubebit.core.usecases.release;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.exception.ReleaseNotFoundException;
import nl.kubebit.core.entities.release.exception.RevisionNotFoundException;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;

/**
 * 
 */
@UseCase
class GetReleaseRevisionUseCaseImpl implements GetReleaseRevisionUseCase {
    // --------------------------------------------------------------------------------------------
    
    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;
    private final ReleaseGateway releaseGateway;

    /**
     *
     */
    public GetReleaseRevisionUseCaseImpl(ProjectGateway projectGateway, NamespaceGateway namespaceGateway, ReleaseGateway releaseGateway) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.releaseGateway = releaseGateway;
    }

    /**
     * 
     */
    @Override
    public ReleaseRefValuesResponse execute(String projectId, String namespaceName, String releaseId, Long revisionVersion) {
        log.info("{} - {} -> get revision: {}", projectId, namespaceName, revisionVersion);
        var project = projectGateway.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        var namespace = namespaceGateway.findByName(project.id(), namespaceName).orElseThrow(NamespaceNotFoundException::new);
        var release = releaseGateway.findById(namespace.id(), releaseId).orElseThrow(ReleaseNotFoundException::new);
        
        return releaseGateway.findRevisionById(namespace.id(), release, revisionVersion).map(ReleaseRefValuesResponse::new)
            .orElseThrow(RevisionNotFoundException::new);
    }
    
}
