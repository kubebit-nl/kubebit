package nl.kubebit.core.entities.release.exception;

import nl.kubebit.core.entities.common.exception.EntityNotDeletedException;

/**
 * 
 */
public class ReleaseNotDeletedException extends EntityNotDeletedException {

    public ReleaseNotDeletedException() {
        super("Release not deleted");
    }
    
}
