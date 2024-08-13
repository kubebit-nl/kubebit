package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import nl.kubebit.core.usecases.template.CreateTemplateUsecase;
import nl.kubebit.core.usecases.template.CreateTemplateUsecase.TemplateCreateRequest;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/template")
public class CreateTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final CreateTemplateUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public CreateTemplateController(CreateTemplateUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @PostMapping
    public TemplateResponse createTemplate(
        @RequestBody @Valid TemplateCreateRequest request) {
        return usecase.execute(request);
    }
    
}
