package nl.kubebit.core.usecases.template.admin;

import java.util.List;

import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

/**
 * 
 */
public interface FetchTemplatesUseCase {
    
    List<TemplateItemResponse> execute();
    
}
