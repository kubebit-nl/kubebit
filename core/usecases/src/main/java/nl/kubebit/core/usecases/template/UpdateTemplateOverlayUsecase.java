package nl.kubebit.core.usecases.template;

import java.util.Map;

import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
public interface UpdateTemplateOverlayUsecase {
    
    TemplateResponse execute(String templateId, Map<String, Object> overlay) throws TemplateNotFoundException;

}
