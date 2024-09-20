package nl.kubebit.core.infrastructure.template.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.admin.UpdateTemplateSchemaUseCase;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

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
    private final UpdateTemplateSchemaUseCase useCase;

    /**
     *
     */
    public UpdateTemplateSchemaController(UpdateTemplateSchemaUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     *
     */
    @PutMapping
    public TemplateItemResponse updateTemplateFor(
        @PathVariable("template_id") String templateId,
        @RequestBody Map<String, Object> form){
        return useCase.execute(templateId, form);
    }
    
}
