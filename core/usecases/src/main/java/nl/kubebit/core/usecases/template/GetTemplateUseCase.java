package nl.kubebit.core.usecases.template;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateFullResponse;

/**
 * 
 */
public interface GetTemplateUseCase {
    
    TemplateFullResponse execute(

            @NotBlank
            String templateId

    ) throws TemplateNotFoundException;

}
