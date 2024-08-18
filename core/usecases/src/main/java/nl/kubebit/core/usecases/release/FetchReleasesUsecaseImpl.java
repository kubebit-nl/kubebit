package nl.kubebit.core.usecases.release;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.enviroment.exception.EnviromentNotFoundException;
import nl.kubebit.core.entities.enviroment.gateway.EnviromentGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
@Usecase
public class FetchReleasesUsecaseImpl implements FetchReleasesUsecase {
    // --------------------------------------------------------------------------------------------
    
    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final EnviromentGateway enviromentGateway;
    private final ReleaseGateway releaseGateway;
    
    /**
     * 
     * @param projectGateway
     * @param enviromentGateway
     * @param deploymentGateway
     */
    public FetchReleasesUsecaseImpl(ProjectGateway projectGateway, EnviromentGateway enviromentGateway, ReleaseGateway releaseGateway) {
        this.projectGateway = projectGateway;
        this.enviromentGateway = enviromentGateway;
        this.releaseGateway = releaseGateway;
    }

    /**
     * 
     */
    @Override
    public List<ReleaseResponse> execute(String projectId, String enviromentName) {
        log.info("{} - {} -> fetch releases", projectId, enviromentName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var enviroment = enviromentGateway.findByName(project, enviromentName).orElseThrow(() -> new EnviromentNotFoundException(enviromentName));
        return releaseGateway.findAll(enviroment.id()).stream().map(ReleaseResponse::new).toList();
    }
   
    
}
