package nl.kubebit.core.entities.release.exception;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;

/**
 * 
 */
public class ReleaseNotFoundException extends EntityNotFoundException {

    public ReleaseNotFoundException(String releaseId) {
        super("Release '" + releaseId + "' not found");
    }
    
}
