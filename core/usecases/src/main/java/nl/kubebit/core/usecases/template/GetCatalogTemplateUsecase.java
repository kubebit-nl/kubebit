package nl.kubebit.core.usecases.template;

import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateFormResponse;

/**
 * 
 */
public interface GetCatalogTemplateUsecase {
    
    TemplateFormResponse execute(String templateId) throws TemplateNotFoundException;

}
