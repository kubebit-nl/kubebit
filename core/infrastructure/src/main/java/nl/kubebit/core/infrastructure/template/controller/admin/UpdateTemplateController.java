package nl.kubebit.core.infrastructure.template.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.kubebit.core.usecases.template.admin.UpdateTemplateUseCase;
import nl.kubebit.core.usecases.template.admin.UpdateTemplateUseCase.TemplateUpdateRequest;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/admin/v1/templates/{template_id}")
public class UpdateTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final UpdateTemplateUseCase useCase;

    /**
     *
     */
    public UpdateTemplateController(UpdateTemplateUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     *
     */
    @PutMapping
    public TemplateItemResponse createTemplate(
        @PathVariable("template_id") String templateId,
        @RequestBody @Valid TemplateUpdateRequest request){
        return useCase.execute(templateId, request);
    }
    
}
