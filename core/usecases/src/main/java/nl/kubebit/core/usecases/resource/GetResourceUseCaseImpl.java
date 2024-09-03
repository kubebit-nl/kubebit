package nl.kubebit.core.usecases.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.resource.Resource;
import nl.kubebit.core.entities.resource.exception.ResourceNotFoundException;
import nl.kubebit.core.entities.resource.gateway.ResourceGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;

/**
 * 
 */
@UseCase
class GetResourceUseCaseImpl implements GetResourceUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;
    private final ResourceGateway resourcegateway;

    /**
     *
     */
    public GetResourceUseCaseImpl(
            ProjectGateway projectGateway, 
            NamespaceGateway namespaceGateway,
            ReleaseGateway releaseGateway, 
            ResourceGateway resourcegateway) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.resourcegateway = resourcegateway;
    }

    /**
     *
     */
    @Override
    public Resource execute(String projectId, String namespaceName, ReleaseResourceRef ref) {
        log.info("{} - {} -> get resource: {}", projectId, namespaceName, ref);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var namespace = namespaceGateway.findByName(project, namespaceName).orElseThrow(() -> new NamespaceNotFoundException(namespaceName));
        return resourcegateway.getResource(namespace.id(), ref).orElseThrow(ResourceNotFoundException::new);
    }
}
