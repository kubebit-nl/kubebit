package nl.kubebit.core.usecases.release;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.exception.ReleaseNotFoundException;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;

/**
 * 
 */
@UseCase
class GetReleaseUseCaseImpl implements GetReleaseUseCase {
    // --------------------------------------------------------------------------------------------
    
    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;
    private final ReleaseGateway releaseGateway;

    /**
     * Constructor
     * @param projectGateway project gateway
     * @param namespaceGateway namespace gateway
     * @param releaseGateway release gateway
     */
    public GetReleaseUseCaseImpl(ProjectGateway projectGateway, NamespaceGateway namespaceGateway, ReleaseGateway releaseGateway) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.releaseGateway = releaseGateway;
    }

    /**
     * 
     */
    @Override
    public ReleaseValueResponse execute(String projectId, String namespaceName, String releaseId) {
        log.info("{} - {} -> get release: {}", projectId, namespaceName, releaseId);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var namespace = namespaceGateway.findByName(project, namespaceName).orElseThrow(() -> new NamespaceNotFoundException(namespaceName));
        return releaseGateway.findById(namespace.id(), releaseId).map(ReleaseValueResponse::new).orElseThrow(() -> new ReleaseNotFoundException(releaseId));
    }
    
}
