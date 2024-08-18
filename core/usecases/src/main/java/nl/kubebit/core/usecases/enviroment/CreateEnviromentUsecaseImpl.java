package nl.kubebit.core.usecases.enviroment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.enviroment.Enviroment;
import nl.kubebit.core.entities.enviroment.exception.EnviromentAlreadyExistsException;
import nl.kubebit.core.entities.enviroment.exception.EnviromentNotCreatedException;
import nl.kubebit.core.entities.enviroment.gateway.EnviromentGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.enviroment.dto.EnviromentResponse;

/**
 * 
 */
@Usecase
public class CreateEnviromentUsecaseImpl implements CreateEnviromentUsecase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final EnviromentGateway enviromentGateway;

    /**
     * 
     * @param projectGateway
     * @param enviromentGateway
     */
    public CreateEnviromentUsecaseImpl(ProjectGateway projectGateway, EnviromentGateway enviromentGateway) {
        this.projectGateway = projectGateway;
        this.enviromentGateway = enviromentGateway;
    }

    /**
     * 
     */
    @Override
    public EnviromentResponse execute(String projectId, EnviromentRequest request) throws EnviromentNotCreatedException {
        log.info("{} -> create enviroment {}", projectId, request.name());
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        //
        var enviroment = new Enviroment(
            request.name(), 
            request.description(),
            project.id());

        //
        if(!enviromentGateway.unique(enviroment.id())) {
            throw new EnviromentAlreadyExistsException(enviroment.name());
        };

        //
        return enviromentGateway.save(enviroment).map(EnviromentResponse::new).orElseThrow(EnviromentNotCreatedException::new);
    }
    
}
