package nl.kubebit.core.entities.resource.exception;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;

/**
 * 
 */
public class ResourceNotFoundException extends EntityNotFoundException {

    public ResourceNotFoundException() {
        super("Resource not found");
    }
    
}
