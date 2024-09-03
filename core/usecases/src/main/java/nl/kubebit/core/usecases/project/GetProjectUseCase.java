package nl.kubebit.core.usecases.project;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

/**
 * 
 */
public interface GetProjectUseCase {

    ProjectResponse execute(

            @NotBlank
            String projectId

    ) throws ProjectNotFoundException;
    
}
