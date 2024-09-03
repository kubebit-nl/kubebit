package nl.kubebit.core.infrastructure.template.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.template.GetTemplateRawValuesUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/templates/{template_id}/values")
public class GetTemplateRawValuesController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetTemplateRawValuesUseCase UseCase;

    /**
     *
     */
    public GetTemplateRawValuesController(GetTemplateRawValuesUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @Operation(summary = "Get chart values.yaml")
    @GetMapping
    public String getTemplate(
        @PathVariable("template_id") String templateId){
        return UseCase.execute(templateId);
    }
    
}
