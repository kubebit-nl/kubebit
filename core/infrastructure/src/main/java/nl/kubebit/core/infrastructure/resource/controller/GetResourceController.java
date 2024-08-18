package nl.kubebit.core.infrastructure.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.resource.Resource;
import nl.kubebit.core.usecases.resource.GetResourceUsecase;

/**
 * 
 */
@Tag(name = "Resource")
@RestController
@RequestMapping("/api/v1/project/{project_id}/enviroment/{enviroment_name}/resource")
public class GetResourceController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetResourceUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public GetResourceController(GetResourceUsecase usecase) {
        this.usecase = usecase;
    }
    
    /**
     * 
     * @param projectId
     * @param enviromentName
     * @param ref
     * @return 
     */
    @GetMapping
    public Resource getResource(
        @PathVariable("project_id") String projectId,
        @PathVariable("enviroment_name") String enviromentName,
        @RequestParam(required = false, defaultValue = "") String group,
        @RequestParam String version,
        @RequestParam String kind,
        @RequestParam String name) {
        return usecase.execute(projectId, enviromentName, new ReleaseResourceRef(group, version, kind, name));
    }
}
