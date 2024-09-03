package nl.kubebit.core.infrastructure.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.resource.GetContainerLogsUseCase;

/**
 * 
 */
@Tag(name = "Resource")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces/{namespace_name}/logs")
public class GetContainerLogsController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetContainerLogsUseCase UseCase;

    /**
     *
     */
    public GetContainerLogsController(GetContainerLogsUseCase UseCase) {
        this.UseCase = UseCase;
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
        return UseCase.execute(projectId, namespaceName, pod, container);
    }
}
