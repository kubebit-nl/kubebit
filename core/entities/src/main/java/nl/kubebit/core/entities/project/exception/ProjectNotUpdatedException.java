package nl.kubebit.core.entities.project.exception;

import nl.kubebit.core.entities.common.exception.EntityNotUpdatedException;

/**
 * 
 */
public class ProjectNotUpdatedException extends EntityNotUpdatedException {

    public ProjectNotUpdatedException() {
        super("Project not updated");
    }
    
}
