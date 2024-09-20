package nl.kubebit.core.usecases.template.admin;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.entities.template.exception.ChartNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;

/**
 * 
 */
public interface GetChartFileUseCase {

    String execute(

            @NotBlank
            String templateId,

            @NotBlank
            String fileName

    ) throws TemplateNotFoundException, ChartNotFoundException;

}
