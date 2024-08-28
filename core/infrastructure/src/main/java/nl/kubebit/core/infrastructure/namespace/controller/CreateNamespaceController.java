package nl.kubebit.core.infrastructure.namespace.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.kubebit.core.usecases.namespace.CreateNamespaceUsecase;
import nl.kubebit.core.usecases.namespace.CreateNamespaceUsecase.NamespaceRequest;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Namespace")
@RestController
@RequestMapping("/api/v1/project/{project_id}/namespace")
public class CreateNamespaceController {
    // --------------------------------------------------------------------------------------------

    //
    private final CreateNamespaceUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public CreateNamespaceController(CreateNamespaceUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @PostMapping
    public NamespaceResponse getProject(
        @PathVariable("project_id") String projectId,
        @RequestBody @Valid NamespaceRequest request) {
        return usecase.execute(projectId, request);
    }
    
}
