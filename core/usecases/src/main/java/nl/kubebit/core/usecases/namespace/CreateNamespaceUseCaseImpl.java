package nl.kubebit.core.usecases.namespace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.namespace.exception.NamespaceAlreadyExistsException;
import nl.kubebit.core.entities.namespace.exception.NamespaceNotCreatedException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

/**
 * 
 */
@UseCase
class CreateNamespaceUseCaseImpl implements CreateNamespaceUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;

    /**
     *
     */
    public CreateNamespaceUseCaseImpl(ProjectGateway projectGateway, NamespaceGateway namespaceGateway) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
    }

    /**
     * 
     */
    @Override
    public NamespaceResponse execute(String projectId, NamespaceRequest request) throws NamespaceNotCreatedException {
        log.info("{} -> create namespace {}", projectId, request.name());
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        //
        var namespace = new Namespace(
            request.name(), 
            request.description(),
            project.id(),
            request.isDefault());

        //
        if(!namespaceGateway.unique(namespace.id())) {
            throw new NamespaceAlreadyExistsException(namespace.name());
        }

        //
        return namespaceGateway.save(namespace).map(NamespaceResponse::new).orElseThrow(NamespaceNotCreatedException::new);
    }
    
}
