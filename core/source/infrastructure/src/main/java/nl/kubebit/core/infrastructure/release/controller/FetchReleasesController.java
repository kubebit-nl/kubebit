package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.release.FetchReleasesUsecase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/project/{project_id}/enviroment/{enviroment_name}/release")
public class FetchReleasesController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchReleasesUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public FetchReleasesController(FetchReleasesUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @GetMapping
    public List<ReleaseResponse> fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("enviroment_name") String enviromentName) {
        return usecase.execute(projectId, enviromentName);
    }
    
}
