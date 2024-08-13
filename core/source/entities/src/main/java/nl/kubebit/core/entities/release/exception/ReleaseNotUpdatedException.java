package nl.kubebit.core.entities.release.exception;

import nl.kubebit.core.entities.common.exception.EntityNotUpdatedException;

/**
 * 
 */
public class ReleaseNotUpdatedException extends EntityNotUpdatedException {

    public ReleaseNotUpdatedException() {
        super("Release not updated");
    }
    
}
