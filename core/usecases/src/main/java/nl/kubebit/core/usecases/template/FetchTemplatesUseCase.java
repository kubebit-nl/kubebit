package nl.kubebit.core.usecases.template;

import java.util.List;

import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
public interface FetchTemplatesUseCase {
    
    List<TemplateResponse> execute();
    
}
