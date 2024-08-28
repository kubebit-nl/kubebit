package nl.kubebit.core.entities.namespace.exception;

import nl.kubebit.core.entities.common.exception.EntityAlreadyExistsException;

/**
 * 
 */
public class NamespaceAlreadyExistsException extends EntityAlreadyExistsException {

    public NamespaceAlreadyExistsException(String namespaceName) {
        super("Namespace '" + namespaceName + "' already exists");
    }
    
}
