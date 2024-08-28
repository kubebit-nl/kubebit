package nl.kubebit.core.entities.namespace.exception;

import nl.kubebit.core.entities.common.exception.EntityNotUpdatedException;

/**
 * 
 */
public class NamespaceNotUpdatedException extends EntityNotUpdatedException {

    public NamespaceNotUpdatedException() {
        super("Enviroment not updated");
    }
    
}
