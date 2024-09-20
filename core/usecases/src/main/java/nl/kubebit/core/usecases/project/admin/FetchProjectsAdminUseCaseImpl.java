package nl.kubebit.core.usecases.project.admin;

import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 
 */
@UseCase
class FetchProjectsAdminUseCaseImpl implements FetchProjectsAdminUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway gateway;

    /**
     *
     */
    public FetchProjectsAdminUseCaseImpl(ProjectGateway gateway) {
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
