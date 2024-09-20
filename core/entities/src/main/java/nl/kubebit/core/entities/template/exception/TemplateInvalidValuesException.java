package nl.kubebit.core.entities.template.exception;

import nl.kubebit.core.entities.common.exception.EntityInvalidStatusException;

/**
 * 
 */
public class TemplateInvalidValuesException extends EntityInvalidStatusException {

    public TemplateInvalidValuesException(String message) {
        super("Template values is invalid format: " + message);
    }
    
}
