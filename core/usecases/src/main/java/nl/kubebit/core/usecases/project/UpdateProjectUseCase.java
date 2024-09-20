package nl.kubebit.core.usecases.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

/**
 *
 */
public interface UpdateProjectUseCase {

    /**
     * Update project
     *
     * @param projectId project id
     * @param request   request
     * @return ProjectResponse
     * @throws ProjectNotFoundException project not found
     */
    ProjectResponse execute(

            @NotBlank
            String projectId,

            @NotNull
            UpdateProjectRequest request

    ) throws ProjectNotFoundException;

    /**
     * UpdateProjectRequest
     *
     * @param name        project name
     * @param description project description
     */
    record UpdateProjectRequest(

            @JsonProperty("name")
            String name,

            @JsonProperty("description")
            String description

    ) {
    }
}
