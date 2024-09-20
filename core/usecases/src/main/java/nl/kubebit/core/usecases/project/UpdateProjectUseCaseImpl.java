package nl.kubebit.core.usecases.project;

import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.exception.ProjectNotUpdatedException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@UseCase
class UpdateProjectUseCaseImpl implements UpdateProjectUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway gateway;

    /**
     *
     */
    public UpdateProjectUseCaseImpl(ProjectGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Update project
     *
     * @param projectId project id
     * @param request   request
     * @return ProjectResponse project response
     * @throws ProjectNotFoundException   project not found
     * @throws ProjectNotUpdatedException project not updated
     */
    @Override
    public ProjectResponse execute(String projectId, UpdateProjectRequest request) throws ProjectNotFoundException {
        log.info("get project: {}", projectId);
        var project = gateway.findById(projectId).orElseThrow(ProjectNotFoundException::new);

        //
        var entity = new Project(
                project.id(),
                request.name(),
                request.description()
        );

        //
        return gateway.update(entity).map(ProjectResponse::new).orElseThrow(ProjectNotUpdatedException::new);
    }

}
