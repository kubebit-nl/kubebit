package nl.kubebit.core.infrastructure.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.GetTemplateUsecase;
import nl.kubebit.core.usecases.template.dto.TemplateFullResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/v1/templates/{template_id}")
public class GetTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetTemplateUsecase usecase;

    /**
     *
     */
    public GetTemplateController(GetTemplateUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     *
     */
    @GetMapping
    public TemplateFullResponse getTemplate(
        @PathVariable("template_id") String templateId){
        return usecase.execute(templateId);
    }
    
}
