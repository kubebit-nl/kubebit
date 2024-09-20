package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.release.FetchReleaseRevisionsUseCase;
import nl.kubebit.core.usecases.release.dto.RevisionResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces/{namespace_name}/releases/{release_id}/revision")
public class FetchReleaseRevisionsController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchReleaseRevisionsUseCase useCase;

    /**
     *
     */
    public FetchReleaseRevisionsController(FetchReleaseRevisionsUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     *
     */
    @GetMapping
    public List<RevisionResponse> fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @PathVariable("release_id") String releaseId) {
        return useCase.execute(projectId, namespaceName, releaseId);
    }
    
}
