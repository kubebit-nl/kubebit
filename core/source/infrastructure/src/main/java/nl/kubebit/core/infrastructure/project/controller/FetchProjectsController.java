package nl.kubebit.core.infrastructure.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.project.FetchProjectsUsecase;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 */
@Tag(name = "Project")
@RestController
@RequestMapping("/api/v1/project")
public class FetchProjectsController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchProjectsUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public FetchProjectsController(FetchProjectsUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @GetMapping
    public List<ProjectResponse> fetchProjects() {
        return usecase.execute();
    }
    
}
