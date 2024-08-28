package nl.kubebit.core.usecases.namespace;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

/**
 * 
 */
public interface GetNamespaceUsecase {

    NamespaceResponse execute(String projectId, String namespaceId) throws NamespaceNotFoundException;
    
}
