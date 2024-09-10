package nl.kubebit.core.entities.template.exception;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;

/**
 * 
 */
public class TemplateNotFoundException extends EntityNotFoundException {

    public TemplateNotFoundException() {
        super("Template not found");
    }
    
}
