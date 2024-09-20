package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.release.GetReleaseRevisionUseCase;
import nl.kubebit.core.usecases.release.dto.RevisionItemResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces/{namespace_name}/releases/{release_id}/revision/{revision_version}")
public class GetReleaseRevisionController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetReleaseRevisionUseCase useCase;

    /**
     *
     */
    public GetReleaseRevisionController(GetReleaseRevisionUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     *
     */
    @GetMapping
    public RevisionItemResponse fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @PathVariable("release_id") String releaseId,
        @PathVariable("revision_version") Long revisionVersion) {
        return useCase.execute(projectId, namespaceName, releaseId, revisionVersion);
    }
    
}
