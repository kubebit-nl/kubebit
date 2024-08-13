package nl.kubebit.core.entities.release.exception;

import nl.kubebit.core.entities.common.exception.EntityNotCreatedException;

/**
 * 
 */
public class ReleaseNotCreatedException extends EntityNotCreatedException {

    public ReleaseNotCreatedException() {
        super("Release not created");
    }
    
}
