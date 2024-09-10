package nl.kubebit.core.infrastructure.template.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.admin.UpdateTemplateValuesUseCase;
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
@RequestMapping("/api/admin/v1/templates/{template_id}/values/{value_type}")
public class UpdateTemplateValuesController {
    // --------------------------------------------------------------------------------------------

    //
    private final UpdateTemplateValuesUseCase UseCase;

    /**
     *
     */
    public UpdateTemplateValuesController(UpdateTemplateValuesUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @PutMapping
    public TemplateResponse updateTemplateOverlay(
        @PathVariable("template_id") String templateId,
        @PathVariable("value_type") UpdateTemplateValuesUseCase.TemplateValueType valueType,
        @RequestBody Map<String, Object> overlay){
        return UseCase.execute(templateId, valueType, overlay);
    }
    
}
