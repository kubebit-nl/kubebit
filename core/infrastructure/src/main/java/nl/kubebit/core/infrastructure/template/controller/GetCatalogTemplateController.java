package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.GetCatalogTemplateUsecase;
import nl.kubebit.core.usecases.template.dto.TemplateFormResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/template/catalog/{template_id}")
public class GetCatalogTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetCatalogTemplateUsecase usecase;

    /**
     *
     */
    public GetCatalogTemplateController(GetCatalogTemplateUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     *
     */
    @GetMapping
    public TemplateFormResponse getTemplate(
        @PathVariable("template_id") String templateId){
        return usecase.execute(templateId);
    }
    
}
