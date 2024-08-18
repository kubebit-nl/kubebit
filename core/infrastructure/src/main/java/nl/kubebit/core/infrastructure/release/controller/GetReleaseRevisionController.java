package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.release.GetReleaseRevisionUsecase;
import nl.kubebit.core.usecases.release.GetReleaseRevisionUsecase.ReleaseRefValuesResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/project/{project_id}/enviroment/{enviroment_name}/release/{release_id}/revision/{revision_version}")
public class GetReleaseRevisionController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetReleaseRevisionUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public GetReleaseRevisionController(GetReleaseRevisionUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @GetMapping
    public ReleaseRefValuesResponse fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("enviroment_name") String enviromentName,
        @PathVariable("release_id") String releaseId,
        @PathVariable("revision_version") Long revisionVersion) {
        return usecase.execute(projectId, enviromentName, releaseId, revisionVersion);
    }
    
}
