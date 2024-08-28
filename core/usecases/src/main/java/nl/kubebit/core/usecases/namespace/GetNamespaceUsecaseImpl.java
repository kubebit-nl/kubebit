package nl.kubebit.core.usecases.namespace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

/**
 * 
 */
@Usecase
class GetNamespaceUsecaseImpl implements GetNamespaceUsecase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;

    /**
     *
     */
    public GetNamespaceUsecaseImpl(ProjectGateway projectGateway, NamespaceGateway namespaceGateway) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
    }

    /**
     *
     */
    @Override
    public NamespaceResponse execute(String projectId, String namespaceName) {
        log.info("{} -> get namespace: {}", projectId, namespaceName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        return namespaceGateway.findByName(project, namespaceName).map(NamespaceResponse::new).orElseThrow(() -> new NamespaceNotFoundException(namespaceName));
    }
    
}
