package nl.kubebit.core.usecases.project;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;
import nl.kubebit.core.usecases.common.annotation.UseCase;

/**
 * 
 */
@UseCase
class FetchProjectsUseCaseImpl implements FetchProjectsUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway gateway;

    /**
     *
     */
    public FetchProjectsUseCaseImpl(ProjectGateway gateway) {
        this.gateway = gateway;
    }

    /**
     *
     */
    @Override
    public List<ProjectResponse> execute() {
        log.info("fetch projects");
        return gateway.findAll().stream().map(ProjectResponse::new).toList();
    }

}
