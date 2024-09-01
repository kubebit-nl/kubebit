package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.template.FetchCatalogTemplatesUsecase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

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
    private final FetchCatalogTemplatesUsecase usecase;

    /**
     *
     */
    public FetchCatalogTemplatesController(FetchCatalogTemplatesUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     *
     */
    @GetMapping
    public List<TemplateResponse> fetchCatalogTemplates() {
        return usecase.execute();
    }
    
}
