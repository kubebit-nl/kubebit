package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.GetTemplateUsecase;
import nl.kubebit.core.usecases.template.dto.TemplateSchemaResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/template/{template_id}")
public class GetTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetTemplateUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public GetTemplateController(GetTemplateUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @GetMapping
    public TemplateSchemaResponse createTemplate(
        @PathVariable("template_id") String templateId){
        return usecase.execute(templateId);
    }
    
}
