package nl.kubebit.core.infrastructure.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.kubebit.core.usecases.project.CreateProjectUsecase;
import nl.kubebit.core.usecases.project.CreateProjectUsecase.ProjectRequest;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Project")
@RestController
@RequestMapping("/api/v1/project")
public class CreateProjectController {
    // --------------------------------------------------------------------------------------------

    //
    private final CreateProjectUsecase usecase;

    /**
     *
     */
    public CreateProjectController(CreateProjectUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     *
     */
    @PostMapping
    public ProjectResponse getProject(@RequestBody @Valid ProjectRequest request) {
        return usecase.execute(request);
    }
    
}
