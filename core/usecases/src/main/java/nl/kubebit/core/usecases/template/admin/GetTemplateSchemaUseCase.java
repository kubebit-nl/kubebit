package nl.kubebit.core.usecases.template.admin;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.entities.template.exception.ChartNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;

import java.util.Map;

/**
 * 
 */
public interface GetTemplateSchemaUseCase {

    Map<String, Object> execute(

            @NotBlank
            String templateId

    ) throws TemplateNotFoundException, ChartNotFoundException;

}
