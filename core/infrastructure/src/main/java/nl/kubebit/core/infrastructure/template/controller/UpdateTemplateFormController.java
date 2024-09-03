package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.UpdateTemplateFormUseCase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/templates/{template_id}/form")
public class UpdateTemplateFormController {
    // --------------------------------------------------------------------------------------------

    //
    private final UpdateTemplateFormUseCase UseCase;

    /**
     *
     */
    public UpdateTemplateFormController(UpdateTemplateFormUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @PutMapping
    public TemplateResponse updateTemplateFor(
        @PathVariable("template_id") String templateId,
        @RequestBody Map<String, Object> form){
        return UseCase.execute(templateId, form);
    }
    
}
