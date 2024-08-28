package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.template.FetchTemplatesUsecase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/template")
public class FetchTemplatesController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchTemplatesUsecase usecase;

    /**
     *
     */
    public FetchTemplatesController(FetchTemplatesUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     *
     */
    @GetMapping
    public List<TemplateResponse> fetchTemplates() {
        return usecase.execute();
    }
    
}
