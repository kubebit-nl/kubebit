package nl.kubebit.core.infrastructure.template.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.admin.GetTemplateSchemaUseCase;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/admin/v1/templates/{template_id}/schema")
public class GetTemplateSchemaController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetTemplateSchemaUseCase UseCase;

    /**
     *
     */
    public GetTemplateSchemaController(GetTemplateSchemaUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @Operation(summary = "Get chart values.schema.json")
    @GetMapping
    public Map<String, Object> getTemplate(
        @PathVariable("template_id") String templateId){
        return UseCase.execute(templateId);
    }
    
}
