package nl.kubebit.core.infrastructure.namespace.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.namespace.GetNamespaceUseCase;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Namespace")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces")
public class GetNamespaceController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetNamespaceUseCase UseCase;

    /**
     *
     */
    public GetNamespaceController(GetNamespaceUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @GetMapping("/{namespace_name}")
    public NamespaceResponse getProject(
        @PathVariable("project_id") String projectId,
        @PathVariable("namespace_name") String namespaceName) {
        return UseCase.execute(projectId, namespaceName);
    }
    
}
