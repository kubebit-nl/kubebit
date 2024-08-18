package nl.kubebit.core.usecases.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.enviroment.exception.EnviromentNotFoundException;
import nl.kubebit.core.entities.enviroment.gateway.EnviromentGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.resource.Resource;
import nl.kubebit.core.entities.resource.exception.ResourceNotFoundException;
import nl.kubebit.core.entities.resource.gateway.ResourceGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;

/**
 * 
 */
@Usecase
public class GetResourceUsecaseImpl implements GetResourceUsecase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final EnviromentGateway enviromentGateway;
    private final ResourceGateway resourcegateway;

    /**
     * 
     * @param projectGateway
     * @param enviromentGateway
     * @param releaseGateway
     * @param resourcegateway
     */
    public GetResourceUsecaseImpl(
            ProjectGateway projectGateway, 
            EnviromentGateway enviromentGateway,
            ReleaseGateway releaseGateway, 
            ResourceGateway resourcegateway) {
        this.projectGateway = projectGateway;
        this.enviromentGateway = enviromentGateway;
        this.resourcegateway = resourcegateway;
    }

    /**
     * 
     * @param enviromentId
     * @param kind
     * @param name
     */
    @Override
    public Resource execute(String projectId, String enviromentName, ReleaseResourceRef ref) {
        log.info("{} - {} -> get resource: {}", projectId, enviromentName, ref);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var enviroment = enviromentGateway.findByName(project, enviromentName).orElseThrow(() -> new EnviromentNotFoundException(enviromentName));
        return resourcegateway.getResource(enviroment.id(), ref).orElseThrow(ResourceNotFoundException::new);
    }
}
