package nl.kubebit.core.usecases.enviroment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.enviroment.exception.EnviromentNotFoundException;
import nl.kubebit.core.entities.enviroment.gateway.EnviromentGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.enviroment.dto.EnviromentResponse;

/**
 * 
 */
@Usecase
public class GetEnviromentUsecaseImpl implements GetEnviromentUsecase {
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
    public GetEnviromentUsecaseImpl(ProjectGateway projectGateway, EnviromentGateway enviromentGateway) {
        this.projectGateway = projectGateway;
        this.enviromentGateway = enviromentGateway;
    }

    /**
     * 
     * @return
     */
    @Override
    public EnviromentResponse execute(String projectId, String enviromentName) {
        log.info("{} -> get enviroment: {}", projectId, enviromentName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        return enviromentGateway.findByName(project, enviromentName).map(EnviromentResponse::new).orElseThrow(() -> new EnviromentNotFoundException(enviromentName));
    }
    
}
