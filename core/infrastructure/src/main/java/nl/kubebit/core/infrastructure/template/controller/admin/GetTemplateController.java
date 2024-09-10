package nl.kubebit.core.infrastructure.template.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.admin.GetTemplateUseCase;
import nl.kubebit.core.usecases.template.dto.TemplateFullResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/admin/v1/templates/{template_id}")
public class GetTemplateController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetTemplateUseCase UseCase;

    /**
     *
     */
    public GetTemplateController(GetTemplateUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @GetMapping
    public TemplateFullResponse getTemplate(
        @PathVariable("template_id") String templateId){
        return UseCase.execute(templateId);
    }
    
}
