package nl.kubebit.core.usecases.template;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.validator.HasTemplateStatus;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
public interface UpdateTemplateStatusUsecase {
    
    /**
     *
     */
    TemplateResponse execute(

        @NotBlank
        String templateId, 
        
        @NotNull
        @HasTemplateStatus(anyOf = { TemplateStatus.DEPRECATED, TemplateStatus.AVAILABLE })
        TemplateStatus status
        
    ) throws TemplateNotFoundException;
    
}
