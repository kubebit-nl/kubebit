package nl.kubebit.core.usecases.template.admin;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
public interface ValidateTemplateUseCase {
    
    TemplateResponse execute(

            @NotBlank
            String templateId

    ) throws TemplateNotFoundException;
    
}
