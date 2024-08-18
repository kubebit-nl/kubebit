package nl.kubebit.core.usecases.project;

import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

/**
 * 
 */
public interface GetProjectUsecase {

    ProjectResponse execute(String projectId) throws ProjectNotFoundException;
    
}
