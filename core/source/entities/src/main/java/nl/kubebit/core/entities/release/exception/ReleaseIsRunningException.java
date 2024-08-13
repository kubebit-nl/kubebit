package nl.kubebit.core.entities.release.exception;

import nl.kubebit.core.entities.common.exception.EntityInvalidStatusException;

/**
 * 
 */
public class ReleaseIsRunningException extends EntityInvalidStatusException {

    public ReleaseIsRunningException(String releaseId) {
        super("Release '" + releaseId + "' is in a running state");
    }
    
}
