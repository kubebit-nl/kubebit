package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.release.DeleteReleaseUseCase;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces/{namespace_name}/releases/{release_id}")
public class DeleteReleaseController {
    // --------------------------------------------------------------------------------------------

    //
    private final DeleteReleaseUseCase UseCase;

    /**
     *
     */
    public DeleteReleaseController(DeleteReleaseUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @DeleteMapping
    public void fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @PathVariable("release_id") String releaseId) {
        UseCase.execute(projectId, namespaceName, releaseId);
    }
    
}
