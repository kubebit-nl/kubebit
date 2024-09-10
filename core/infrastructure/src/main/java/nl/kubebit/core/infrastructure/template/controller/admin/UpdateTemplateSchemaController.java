package nl.kubebit.core.infrastructure.template.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.admin.UpdateTemplateSchemaUseCase;
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
@RequestMapping("/api/admin/v1/templates/{template_id}/schema")
public class UpdateTemplateSchemaController {
    // --------------------------------------------------------------------------------------------

    //
    private final UpdateTemplateSchemaUseCase UseCase;

    /**
     *
     */
    public UpdateTemplateSchemaController(UpdateTemplateSchemaUseCase UseCase) {
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
