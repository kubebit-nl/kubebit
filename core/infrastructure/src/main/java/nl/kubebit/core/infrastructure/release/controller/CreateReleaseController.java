package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import nl.kubebit.core.usecases.release.CreateReleaseUseCase;
import nl.kubebit.core.usecases.release.CreateReleaseUseCase.ReleaseCreateRequest;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces/{namespace_name}/releases")
public class CreateReleaseController {
    // --------------------------------------------------------------------------------------------

    //
    private final CreateReleaseUseCase UseCase;

    /**
     *
     */
    public CreateReleaseController(CreateReleaseUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @PostMapping
    public ReleaseResponse getProject(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @RequestBody @Valid ReleaseCreateRequest request) {
        return UseCase.execute(projectId, namespaceName, request);
    }
    
}
