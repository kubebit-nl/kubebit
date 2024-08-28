package nl.kubebit.core.usecases.template;

import nl.kubebit.core.entities.template.exception.ChartNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;

/**
 * 
 */
public interface GetTemplateRawValuesUsecase {
    
    String execute(String templateId) throws TemplateNotFoundException, ChartNotFoundException;

}
