package nl.kubebit.core.usecases.template;

import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateSchemaResponse;

/**
 * 
 */
public interface GetTemplateUsecase {
    
    TemplateSchemaResponse execute(String templateId) throws TemplateNotFoundException;

}
