package nl.kubebit.core.usecases.template;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.entities.template.exception.ChartNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;

/**
 * 
 */
public interface GetTemplateRawValuesUseCase {
    
    String execute(

            @NotBlank
            String templateId

    ) throws TemplateNotFoundException, ChartNotFoundException;

}
