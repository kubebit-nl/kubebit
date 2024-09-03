package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import nl.kubebit.core.usecases.template.CreateTemplateUseCase;
import nl.kubebit.core.usecases.template.CreateTemplateUseCase.TemplateCreateRequest;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/templates")
public class CreateTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final CreateTemplateUseCase UseCase;

    /**
     *
     */
    public CreateTemplateController(CreateTemplateUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @PostMapping
    public TemplateResponse createTemplate(
        @RequestBody @Valid TemplateCreateRequest request) {
        return UseCase.execute(request);
    }
    
}
