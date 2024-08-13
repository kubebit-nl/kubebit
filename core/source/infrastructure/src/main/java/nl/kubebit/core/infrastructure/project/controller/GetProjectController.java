package nl.kubebit.core.infrastructure.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.project.GetProjectUsecase;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Project")
@RestController
@RequestMapping("/api/v1/project")
public class GetProjectController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetProjectUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public GetProjectController(GetProjectUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @GetMapping("/{project_id}")
    public ProjectResponse getProject(@PathVariable("project_id") String projectId) {
        return usecase.execute(projectId);
    }
    
}
