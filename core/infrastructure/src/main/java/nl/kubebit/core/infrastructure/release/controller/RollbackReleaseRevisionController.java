package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.release.RollbackReleaseRevisionUseCase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces/{namespace_name}/releases/{release_id}/revision/{revision_version}")
public class RollbackReleaseRevisionController {
    // --------------------------------------------------------------------------------------------

    //
    private final RollbackReleaseRevisionUseCase useCase;

    /**
     *
     */
    public RollbackReleaseRevisionController(RollbackReleaseRevisionUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     *
     */
    @PutMapping
    public ReleaseResponse fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @PathVariable("release_id") String releaseId,
        @PathVariable("revision_version") Long revisionVersion) {
        return useCase.execute(projectId, namespaceName, releaseId, revisionVersion);
    }
    
}
