package nl.kubebit.core.infrastructure.namespace.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.kubebit.core.usecases.namespace.CreateNamespaceUseCase;
import nl.kubebit.core.usecases.namespace.CreateNamespaceUseCase.NamespaceRequest;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Namespace")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces")
public class CreateNamespaceController {
    // --------------------------------------------------------------------------------------------

    //
    private final CreateNamespaceUseCase UseCase;

    /**
     *
     */
    public CreateNamespaceController(CreateNamespaceUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @PostMapping
    public NamespaceResponse getProject(
        @PathVariable("project_id") String projectId,
        @RequestBody @Valid NamespaceRequest request) {
        return UseCase.execute(projectId, request);
    }
    
}
