package nl.kubebit.core.usecases.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import nl.kubebit.core.entities.project.exception.ProjectNotCreatedException;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

/**
 * 
 */
public interface CreateProjectUsecase {
    
    //
    ProjectResponse execute(ProjectRequest request) throws ProjectNotCreatedException;

    /**
     * 
     */
    public record ProjectRequest(

        @NotBlank
        @Size(min = 2, max = 50)
        String name,

        @Size(max = 150)
        String description

    ) {
    }

}
