package nl.kubebit.core.usecases.template;

import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
public interface ValidateTemplateUsecase {
    
    TemplateResponse execute(String templateId) throws TemplateNotFoundException;
    
}
