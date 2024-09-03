package nl.kubebit.core.infrastructure.namespace.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.namespace.FetchNamespacesUseCase;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Namespace")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces")
public class FetchNamespacesController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchNamespacesUseCase UseCase;

    /**
     *
     */
    public FetchNamespacesController(FetchNamespacesUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @GetMapping
    public List<NamespaceResponse> getProject(
        @PathVariable("project_id") String projectId) {
        return UseCase.execute(projectId);
    }
    
}
