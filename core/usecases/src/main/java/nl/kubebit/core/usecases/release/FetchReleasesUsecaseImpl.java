package nl.kubebit.core.usecases.release;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
@Usecase
class FetchReleasesUsecaseImpl implements FetchReleasesUsecase {
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
    public FetchReleasesUsecaseImpl(ProjectGateway projectGateway, NamespaceGateway namespaceGateway, ReleaseGateway releaseGateway) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.releaseGateway = releaseGateway;
    }

    /**
     * 
     */
    @Override
    public List<ReleaseResponse> execute(String projectId, String namespaceName) {
        log.info("{} - {} -> fetch releases", projectId, namespaceName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var namespace = namespaceGateway.findByName(project, namespaceName).orElseThrow(() -> new NamespaceNotFoundException(namespaceName));
        return releaseGateway.findAll(namespace.id()).stream().map(ReleaseResponse::new).toList();
    }
   
    
}
