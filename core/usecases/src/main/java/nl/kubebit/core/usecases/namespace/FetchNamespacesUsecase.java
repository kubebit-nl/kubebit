package nl.kubebit.core.usecases.namespace;

import java.util.List;

import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

/**
 * 
 */
public interface FetchNamespacesUsecase {

    List<NamespaceResponse> execute(String projectId);
    
}
