package nl.kubebit.core.infrastructure.template.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.template.admin.ValidateTemplateUseCase;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/admin/v1/templates/{template_id}/validate")
public class ValidateTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final ValidateTemplateUseCase useCase;

    /**
     *
     */
    public ValidateTemplateController(ValidateTemplateUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     *
     */
    @PostMapping
    public TemplateItemResponse validateTemplate(
        @PathVariable("template_id") String templateId){
        return useCase.execute(templateId);
    }
    
}
