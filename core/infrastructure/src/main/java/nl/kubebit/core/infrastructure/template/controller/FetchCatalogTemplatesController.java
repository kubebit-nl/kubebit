package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.template.FetchCatalogTemplatesUseCase;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/templates/catalog")
public class FetchCatalogTemplatesController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchCatalogTemplatesUseCase UseCase;

    /**
     *
     */
    public FetchCatalogTemplatesController(FetchCatalogTemplatesUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @GetMapping
    public List<TemplateItemResponse> fetchCatalogTemplates() {
        return UseCase.execute();
    }
    
}
