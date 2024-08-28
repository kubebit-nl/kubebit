package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.release.RollbackReleaseRevisionUsecase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/project/{project_id}/namespace/{namespace_name}/release/{release_id}/revision/{revision_version}")
public class RollbackReleaseRevisionController {
    // --------------------------------------------------------------------------------------------

    //
    private final RollbackReleaseRevisionUsecase usecase;

    /**
     *
     */
    public RollbackReleaseRevisionController(RollbackReleaseRevisionUsecase usecase) {
        this.usecase = usecase;
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
        return usecase.execute(projectId, namespaceName, releaseId, revisionVersion);
    }
    
}
