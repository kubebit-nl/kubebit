package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.kubebit.core.usecases.release.UpdateReleasesUsecase;
import nl.kubebit.core.usecases.release.UpdateReleasesUsecase.ReleaseUpdateRequest;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/project/{project_id}/namespace/{namespace_name}/release/{release_id}")
public class UpdateReleaseController {
    // --------------------------------------------------------------------------------------------

    //
    private final UpdateReleasesUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public UpdateReleaseController(UpdateReleasesUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @PutMapping
    public ReleaseResponse fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @PathVariable("release_id") String releaseId,
        @RequestBody @Valid ReleaseUpdateRequest request) {
        return usecase.execute(projectId, namespaceName, releaseId, request);
    }
    
}
