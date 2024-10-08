package nl.kubebit.core.usecases.namespace;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

/**
 * 
 */
@UseCase
class FetchNamespacesUseCaseImpl implements FetchNamespacesUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;

    /**
     *
     */
    public FetchNamespacesUseCaseImpl(ProjectGateway projectGateway, NamespaceGateway namespaceGateway) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
    }

    /**
     *
     */
    @Override
    public List<NamespaceResponse> execute(String projectId) {
        log.info("{} -> fetch namespaces", projectId);
        var project = projectGateway.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        return namespaceGateway.findAll(project).stream().map(NamespaceResponse::new).toList();
    }
    
}
