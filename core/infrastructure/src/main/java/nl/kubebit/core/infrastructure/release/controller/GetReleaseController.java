package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.release.GetReleaseUseCase;
import nl.kubebit.core.usecases.release.dto.ReleaseItemResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces/{namespace_name}/releases/{release_id}")
public class GetReleaseController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetReleaseUseCase useCase;

    /**
     *
     */
    public GetReleaseController(GetReleaseUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     *
     */
    @GetMapping
    public ReleaseItemResponse fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @PathVariable("release_id") String releaseId) {
        return useCase.execute(projectId, namespaceName, releaseId);
    }
    
}
