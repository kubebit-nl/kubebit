package nl.kubebit.core.usecases.release;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.exception.ReleaseNotFoundException;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;

import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.release.dto.ReleaseRefResponse;

/**
 * 
 */
@Usecase
class FetchReleaseRevisionsUsecaseImpl implements FetchReleaseRevisionsUsecase {
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
    public FetchReleaseRevisionsUsecaseImpl(ProjectGateway projectGateway, NamespaceGateway namespaceGateway, ReleaseGateway releaseGateway) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.releaseGateway = releaseGateway;
    }

    /**
     * 
     */
    @Override
    public List<ReleaseRefResponse> execute(String projectId, String namespaceName, String releaseId) {
        log.info("{} - {} -> fetch revisions", projectId, namespaceName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var namespace = namespaceGateway.findByName(project, namespaceName).orElseThrow(() -> new NamespaceNotFoundException(namespaceName));
        var release = releaseGateway.findById(namespace.id(), releaseId).orElseThrow(() -> new ReleaseNotFoundException(releaseId));
        return releaseGateway.findRevisions(namespace.id(), release).stream().map(ReleaseRefResponse::new).toList();
    }
    
}
