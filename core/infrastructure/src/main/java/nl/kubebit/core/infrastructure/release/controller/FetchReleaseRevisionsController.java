package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.release.FetchReleaseRevisionsUsecase;
import nl.kubebit.core.usecases.release.dto.ReleaseRefResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/project/{project_id}/namespace/{namespace_name}/release/{release_id}/revision")
public class FetchReleaseRevisionsController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchReleaseRevisionsUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public FetchReleaseRevisionsController(FetchReleaseRevisionsUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @GetMapping
    public List<ReleaseRefResponse> fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @PathVariable("release_id") String releaseId) {
        return usecase.execute(projectId, namespaceName, releaseId);
    }
    
}
