package nl.kubebit.core.usecases.template;

import org.hibernate.validator.constraints.URL;

import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
public interface UpdateTemplateUsecase {
    
    TemplateResponse execute(String templateId, TemplateUpdateRequest request) throws TemplateNotFoundException;
    
    /**
     * 
     */
    public record TemplateUpdateRequest(

        String type,
        String category,
        @URL String icon

    ) {
        
    }
}
