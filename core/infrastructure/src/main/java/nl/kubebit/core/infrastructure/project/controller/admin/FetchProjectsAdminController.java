package nl.kubebit.core.infrastructure.project.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.project.admin.FetchProjectsAdminUseCase;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 
 */
@Tag(name = "Project")
@RestController
@RequestMapping("/api/admin/v1/projects")
public class FetchProjectsAdminController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchProjectsAdminUseCase UseCase;

    /**
     *
     */
    public FetchProjectsAdminController(FetchProjectsAdminUseCase UseCase) {
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
