package nl.kubebit.core.usecases.template;

import java.util.List;

import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

/**
 * 
 */
public interface FetchCatalogTemplatesUseCase {
    
    List<TemplateItemResponse> execute();
    
}
