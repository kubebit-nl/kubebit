package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import nl.kubebit.core.usecases.release.CreateReleaseUsecase;
import nl.kubebit.core.usecases.release.CreateReleaseUsecase.ReleaseCreateRequest;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/project/{project_id}/namespace/{namespace_name}/release")
public class CreateReleaseController {
    // --------------------------------------------------------------------------------------------

    //
    private final CreateReleaseUsecase usecase;

    /**
     *
     */
    public CreateReleaseController(CreateReleaseUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     *
     */
    @PostMapping
    public ReleaseResponse getProject(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @RequestBody @Valid ReleaseCreateRequest request) {
        return usecase.execute(projectId, namespaceName, request);
    }
    
}
