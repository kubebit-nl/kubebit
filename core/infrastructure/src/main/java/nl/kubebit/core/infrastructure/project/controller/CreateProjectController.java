package nl.kubebit.core.infrastructure.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.kubebit.core.usecases.project.CreateProjectUseCase;
import nl.kubebit.core.usecases.project.CreateProjectUseCase.ProjectRequest;
import nl.kubebit.core.usecases.project.dto.ProjectResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Project")
@RestController
@RequestMapping("/api/v1/projects")
public class CreateProjectController {
    // --------------------------------------------------------------------------------------------

    //
    private final CreateProjectUseCase UseCase;

    /**
     *
     */
    public CreateProjectController(CreateProjectUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @PostMapping
    public ProjectResponse getProject(@RequestBody @Valid ProjectRequest request) {
        return UseCase.execute(request);
    }
    
}
