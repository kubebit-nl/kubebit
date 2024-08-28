package nl.kubebit.core.infrastructure.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.resource.GetContainerLogsUsecase;

/**
 * 
 */
@Tag(name = "Resource")
@RestController
@RequestMapping("/api/v1/project/{project_id}/namespace/{namespace_name}/logs")
public class GetContainerLogsController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetContainerLogsUsecase usecase;

    /**
     *
     */
    public GetContainerLogsController(GetContainerLogsUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     *
     */
    @GetMapping
    public String getResource(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName,
        @RequestParam String pod,
        @RequestParam String container) {
        return usecase.execute(projectId, namespaceName, pod, container);
    }
}
