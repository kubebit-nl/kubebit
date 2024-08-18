package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.release.PatchReleaseUsecase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/project/{project_id}/enviroment/{enviroment_name}/release/{release_id}")
public class PatchReleaseController {
    // --------------------------------------------------------------------------------------------

    //
    private final PatchReleaseUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public PatchReleaseController(PatchReleaseUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @PatchMapping
    public ReleaseResponse fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("enviroment_name") String enviromentName,
        @PathVariable("release_id") String releaseId) {
        return usecase.execute(projectId, enviromentName, releaseId);
    }
    
}
