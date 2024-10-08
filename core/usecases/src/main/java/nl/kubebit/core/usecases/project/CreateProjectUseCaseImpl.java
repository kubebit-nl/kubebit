package nl.kubebit.core.usecases.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.project.exception.ProjectNotCreatedException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;
import nl.kubebit.core.usecases.common.annotation.UseCase;

/**
 * 
 */
@UseCase
class CreateProjectUseCaseImpl implements CreateProjectUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway gateway;

    /**
     *
     */
    public CreateProjectUseCaseImpl(ProjectGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public ProjectResponse execute(ProjectRequest request) {
        log.info("Create project: {}", request);

        //
        var project = new Project(
            java.util.UUID.randomUUID().toString().substring(0, 8), 
            request.name(), 
            request.description());

        //
        return gateway.save(project).map(ProjectResponse::new).orElseThrow(ProjectNotCreatedException::new);
    }
    

    
}
