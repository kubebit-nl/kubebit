package nl.kubebit.core.infrastructure.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.project.GetProjectUseCase;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Project")
@RestController
@RequestMapping("/api/v1/projects")
public class GetProjectController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetProjectUseCase UseCase;

    /**
     *
     */
    public GetProjectController(GetProjectUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @GetMapping("/{project_id}")
    public ProjectResponse getProject(@PathVariable("project_id") String projectId) {
        return UseCase.execute(projectId);
    }
    
}
