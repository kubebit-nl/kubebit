package nl.kubebit.core.entities.template.exception;

import nl.kubebit.core.entities.common.exception.EntityNotCreatedException;

/**
 * 
 */
public class TemplateNotCreatedException extends EntityNotCreatedException {

    public TemplateNotCreatedException() {
        super("Template not created");
    }
    
}
