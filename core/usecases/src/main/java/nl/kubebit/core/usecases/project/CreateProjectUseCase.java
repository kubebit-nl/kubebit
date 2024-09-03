package nl.kubebit.core.usecases.project;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import nl.kubebit.core.entities.project.exception.ProjectNotCreatedException;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

/**
 * 
 */
public interface CreateProjectUseCase {
    
    //
    ProjectResponse execute(

            @NotNull
            @Valid
            ProjectRequest request

    ) throws ProjectNotCreatedException;

    /**
     * 
     */
    record ProjectRequest(

        @NotBlank
        @Size(min = 2, max = 50)
        String name,

        @Size(max = 150)
        String description

    ) {
    }

}
