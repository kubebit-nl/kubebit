package nl.kubebit.core.usecases.template;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
public interface UpdateTemplateUsecase {
    
    TemplateResponse execute(

            @NotBlank
            String templateId,

            @NotNull
            @Valid
            TemplateUpdateRequest request

    ) throws TemplateNotFoundException;
    
    /**
     * 
     */
    @JsonPropertyOrder({"type", "category", "icon"})
    record TemplateUpdateRequest(

        String type,
        String category,
        @URL String icon

    ) {
        
    }
}
