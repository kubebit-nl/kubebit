package nl.kubebit.core.infrastructure.project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.project.UpdateProjectUseCase;
import nl.kubebit.core.usecases.project.UpdateProjectUseCase.UpdateProjectRequest;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@Tag(name = "Project")
@RestController
@RequestMapping("/api/v1/projects")
public class UpdateProjectController {
    // --------------------------------------------------------------------------------------------

    //
    private final UpdateProjectUseCase useCase;

    /**
     *
     */
    public UpdateProjectController(UpdateProjectUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     *
     */
    @PutMapping("/{project_id}")
    public ProjectResponse getProject(
            @PathVariable("project_id") String projectId,
            @RequestBody UpdateProjectRequest request) {
        return useCase.execute(projectId, request);
    }

}
