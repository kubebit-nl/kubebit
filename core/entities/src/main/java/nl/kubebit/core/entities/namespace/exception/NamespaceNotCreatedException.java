package nl.kubebit.core.entities.namespace.exception;

import nl.kubebit.core.entities.common.exception.EntityNotCreatedException;

/**
 * 
 */
public class NamespaceNotCreatedException extends EntityNotCreatedException {

    public NamespaceNotCreatedException() {
        super("Enviroment not created");
    }
    
}
