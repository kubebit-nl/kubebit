package nl.kubebit.core.entities.project.exception;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;

/**
 * 
 */
public class ProjectNotFoundException extends EntityNotFoundException {

    public ProjectNotFoundException() {
        super("Project not found");
    }
    
}
