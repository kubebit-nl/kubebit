package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.kubebit.core.usecases.template.UpdateTemplateUsecase;
import nl.kubebit.core.usecases.template.UpdateTemplateUsecase.TemplateUpdateRequest;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/templates/{template_id}")
public class UpdateTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final UpdateTemplateUsecase usecase;

    /**
     *
     */
    public UpdateTemplateController(UpdateTemplateUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     *
     */
    @PutMapping
    public TemplateResponse createTemplate(
        @PathVariable("template_id") String templateId,
        @RequestBody @Valid TemplateUpdateRequest request){
        return usecase.execute(templateId, request);
    }
    
}
