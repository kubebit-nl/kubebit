package nl.kubebit.core.usecases.template.admin;

import java.util.List;

import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
public interface FetchTemplatesUseCase {
    
    List<TemplateResponse> execute();
    
}
