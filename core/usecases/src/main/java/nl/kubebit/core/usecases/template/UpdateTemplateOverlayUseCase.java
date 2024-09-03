package nl.kubebit.core.usecases.template;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
public interface UpdateTemplateOverlayUseCase {
    
    TemplateResponse execute(

            @NotBlank
            String templateId,

            @NotNull
            Map<String, Object> overlay

    ) throws TemplateNotFoundException;

}
