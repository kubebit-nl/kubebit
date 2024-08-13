package nl.kubebit.core.infrastructure.release.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.entities.release.exception.RevisionNotFoundException;
import nl.kubebit.core.usecases.release.GetReleaseManifestUsecase;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/project/{project_id}/enviroment/{enviroment_name}/release/{release_id}/manifest")
public class GetReleaseManifestController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetReleaseManifestUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public GetReleaseManifestController(GetReleaseManifestUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @GetMapping
    public ResponseEntity<Resource> fetchDeployments(
        @PathVariable("project_id") String projectId,
        @PathVariable("enviroment_name") String enviromentName,
        @PathVariable("release_id") String releaseId,
        @RequestParam(value = "revision_version", required = false) Long revisionVersion) {
        
        //
        var resource = usecase.execute(projectId, enviromentName, releaseId, revisionVersion)
            .orElseThrow(RevisionNotFoundException::new);
        
        //
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"manifest-" + resource.getFilename() + "\"")
                .body(resource);
    }
    
}
