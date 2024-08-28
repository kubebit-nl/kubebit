package nl.kubebit.core.usecases.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.resource.exception.ResourceNotFoundException;
import nl.kubebit.core.entities.resource.gateway.ResourceGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;

/**
 * 
 */
@Usecase
class GetContainerLogsUsecaseImpl implements GetContainerLogsUsecase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;
    private final ResourceGateway resourcegateway;

    /**
     * 
     * @param projectGateway
     * @param namespaceGateway
     * @param releaseGateway
     * @param resourcegateway
     */
    public GetContainerLogsUsecaseImpl(
            ProjectGateway projectGateway, 
            NamespaceGateway namespaceGateway,
            ReleaseGateway releaseGateway, 
            ResourceGateway resourcegateway) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.resourcegateway = resourcegateway;
    }

    @Override
    public String execute(String projectId, String namespaceName, String podName, String containerName) {
        log.info("{} - {} -> get logs: {}/{}", projectId, namespaceName, podName, containerName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var namespace = namespaceGateway.findByName(project, namespaceName).orElseThrow(() -> new NamespaceNotFoundException(namespaceName));
        return resourcegateway.getLogs(namespace.id(), podName, containerName).orElseThrow(ResourceNotFoundException::new);
    }
    
}
