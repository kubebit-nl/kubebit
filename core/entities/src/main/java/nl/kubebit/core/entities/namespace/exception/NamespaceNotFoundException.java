package nl.kubebit.core.entities.namespace.exception;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;

/**
 * 
 */
public class NamespaceNotFoundException extends EntityNotFoundException {

    public NamespaceNotFoundException(String namespaceId) {
        super("Namespace '" + namespaceId + "' not found");
    }
    
}
