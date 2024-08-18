package nl.kubebit.core.entities.project.exception;

import nl.kubebit.core.entities.common.exception.EntityNotCreatedException;

/**
 * 
 */
public class ProjectNotCreatedException extends EntityNotCreatedException {

    public ProjectNotCreatedException() {
        super("Project not created");
    }
    
}
