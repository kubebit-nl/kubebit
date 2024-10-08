package nl.kubebit.core.infrastructure.template.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import nl.kubebit.core.usecases.template.admin.CreateTemplateUseCase;
import nl.kubebit.core.usecases.template.admin.CreateTemplateUseCase.TemplateCreateRequest;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/admin/v1/templates")
public class CreateTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final CreateTemplateUseCase useCase;

    /**
     *
     */
    public CreateTemplateController(CreateTemplateUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     *
     */
    @PostMapping
    public TemplateItemResponse createTemplate(
        @RequestBody @Valid TemplateCreateRequest request) {
        return useCase.execute(request);
    }
    
}
