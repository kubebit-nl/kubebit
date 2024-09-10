package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.GetCatalogTemplateUseCase;
import nl.kubebit.core.usecases.template.dto.TemplateCatalogResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/templates/catalog/{template_id}")
public class GetCatalogTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetCatalogTemplateUseCase UseCase;

    /**
     *
     */
    public GetCatalogTemplateController(GetCatalogTemplateUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @GetMapping
    public TemplateCatalogResponse getTemplate(
        @PathVariable("template_id") String templateId){
        return UseCase.execute(templateId);
    }
    
}
