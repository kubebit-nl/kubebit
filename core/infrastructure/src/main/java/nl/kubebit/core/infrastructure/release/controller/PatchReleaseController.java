package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.release.PatchReleaseUseCase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces/{namespace_name}/releases/{release_id}")
public class PatchReleaseController {
    // --------------------------------------------------------------------------------------------

    //
    private final PatchReleaseUseCase UseCase;

    /**
     *
     */
    public PatchReleaseController(PatchReleaseUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @PatchMapping
    public ReleaseResponse fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @PathVariable("release_id") String releaseId) {
        return UseCase.execute(projectId, namespaceName, releaseId);
    }
    
}
