package nl.kubebit.core.infrastructure.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.project.FetchProjectsUseCase;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 */
@Tag(name = "Project")
@RestController
@RequestMapping("/api/v1/projects")
public class FetchProjectsController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchProjectsUseCase UseCase;

    /**
     *
     */
    public FetchProjectsController(FetchProjectsUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @GetMapping
    public List<ProjectResponse> fetchProjects() {
        return UseCase.execute();
    }
    
}
