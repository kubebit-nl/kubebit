package nl.kubebit.core.usecases.enviroment;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.enviroment.gateway.EnviromentGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.enviroment.dto.EnviromentResponse;

/**
 * 
 */
@Usecase
public class FetchEnviromentsUsecaseImpl implements FetchEnviromentsUsecase {
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
    public FetchEnviromentsUsecaseImpl(ProjectGateway projectGateway, EnviromentGateway enviromentGateway) {
        this.projectGateway = projectGateway;
        this.enviromentGateway = enviromentGateway;
    }

    /**
     * 
     * @return
     */
    @Override
    public List<EnviromentResponse> execute(String projectId) {
        log.info("{} -> fetch enviroments", projectId);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        return enviromentGateway.findAll(project).stream().map(EnviromentResponse::new).toList();
    }
    
}
