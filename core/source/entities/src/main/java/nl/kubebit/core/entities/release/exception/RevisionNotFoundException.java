package nl.kubebit.core.entities.release.exception;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;

/**
 * 
 */
public class RevisionNotFoundException extends EntityNotFoundException {

    public RevisionNotFoundException() {
        super("Revision not found");
    }

    public RevisionNotFoundException(Long revisionVersion) {
        super("Revision '" + revisionVersion.toString() + "' not found");
    }
    
}
