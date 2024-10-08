package nl.kubebit.core.usecases.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;
import nl.kubebit.core.usecases.common.annotation.UseCase;

/**
 * 
 */
@UseCase
class GetProjectUseCaseImpl implements GetProjectUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway gateway;

    /**
     *
     */
    public GetProjectUseCaseImpl(ProjectGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public ProjectResponse execute(String projectId) throws ProjectNotFoundException {
        log.info("get project: {}", projectId);
        return gateway.findById(projectId).map(ProjectResponse::new).orElseThrow(ProjectNotFoundException::new);
    }
    
}
