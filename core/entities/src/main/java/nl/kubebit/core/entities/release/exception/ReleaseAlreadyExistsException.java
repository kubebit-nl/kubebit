package nl.kubebit.core.entities.release.exception;

import nl.kubebit.core.entities.common.exception.EntityAlreadyExistsException;

/**
 * 
 */
public class ReleaseAlreadyExistsException extends EntityAlreadyExistsException {

    public ReleaseAlreadyExistsException() {
        super("Release already exists");
    }
    
}
