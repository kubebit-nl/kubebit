package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.template.ValidateTemplateUseCase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/templates/{template_id}/validate")
public class ValidateTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final ValidateTemplateUseCase UseCase;

    /**
     *
     */
    public ValidateTemplateController(ValidateTemplateUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @PostMapping
    public TemplateResponse validateTemplate(
        @PathVariable("template_id") String templateId){
        return UseCase.execute(templateId);
    }
    
}
