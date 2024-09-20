package nl.kubebit.core.infrastructure.template.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.template.admin.FetchTemplatesUseCase;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/admin/v1/templates")
public class FetchTemplatesController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchTemplatesUseCase UseCase;

    /**
     *
     */
    public FetchTemplatesController(FetchTemplatesUseCase UseCase) {
        this.UseCase = UseCase;
    }

    /**
     *
     */
    @GetMapping
    public List<TemplateItemResponse> fetchTemplates() {
        return UseCase.execute();
    }
    
}
