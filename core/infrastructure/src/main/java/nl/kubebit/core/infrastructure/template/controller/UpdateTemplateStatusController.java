package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.usecases.template.UpdateTemplateStatusUsecase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/template/{template_id}/status/{status}")
public class UpdateTemplateStatusController {
    // --------------------------------------------------------------------------------------------

    //
    private final UpdateTemplateStatusUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public UpdateTemplateStatusController(UpdateTemplateStatusUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @PutMapping
    @Operation(summary = "Update template status [DEPRECATED, AVAILABLE]")
    public TemplateResponse updateTemplateStatus(
        @PathVariable("template_id") String templateId,
        @PathVariable("status") TemplateStatus status){
        return usecase.execute(templateId, status);
    }
    
}
