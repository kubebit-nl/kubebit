package nl.kubebit.core.usecases.template.admin;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

/**
 * 
 */
public interface ValidateTemplateUseCase {
    
    TemplateItemResponse execute(

            @NotBlank
            String templateId

    ) throws TemplateNotFoundException;
    
}
