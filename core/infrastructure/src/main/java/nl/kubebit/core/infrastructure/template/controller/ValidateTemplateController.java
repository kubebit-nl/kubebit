package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.template.ValidateTemplateUsecase;
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
    private final ValidateTemplateUsecase usecase;

    /**
     *
     */
    public ValidateTemplateController(ValidateTemplateUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     *
     */
    @PostMapping
    public TemplateResponse validateTemplate(
        @PathVariable("template_id") String templateId){
        return usecase.execute(templateId);
    }
    
}
