package nl.kubebit.core.infrastructure.template.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.admin.UpdateTemplateValuesUseCase;
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
@RequestMapping("/api/admin/v1/templates/{template_id}/values/{value_type}")
public class UpdateTemplateValuesController {
    // --------------------------------------------------------------------------------------------

    //
    private final UpdateTemplateValuesUseCase useCase;

    /**
     *
     */
    public UpdateTemplateValuesController(UpdateTemplateValuesUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     *
     */
    @PutMapping(consumes = "application/x-yaml")
    public TemplateItemResponse updateTemplateOverlay(
        @PathVariable("template_id") String templateId,
        @PathVariable("value_type") UpdateTemplateValuesUseCase.TemplateValueType valueType,
        @RequestBody String overlay){
        return useCase.execute(templateId, valueType, overlay);
    }
    
}
