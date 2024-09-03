package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.UpdateTemplateOverlayUseCase;
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
@RequestMapping("/api/v1/templates/{template_id}/overlay")
public class UpdateTemplateOverlayController {
    // --------------------------------------------------------------------------------------------

    //
    private final UpdateTemplateOverlayUseCase UseCase;

    /**
     *
     */
    public UpdateTemplateOverlayController(UpdateTemplateOverlayUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @PutMapping
    public TemplateResponse updateTemplateOverlay(
        @PathVariable("template_id") String templateId,
        @RequestBody Map<String, Object> overlay){
        return UseCase.execute(templateId, overlay);
    }
    
}
