package nl.kubebit.core.entities.template.exception;

import nl.kubebit.core.entities.common.exception.EntityNotUpdatedException;

/**
 * 
 */
public class TemplateNotUpdatedException extends EntityNotUpdatedException {

    public TemplateNotUpdatedException() {
        super("Template not updated");
    }
    
}
