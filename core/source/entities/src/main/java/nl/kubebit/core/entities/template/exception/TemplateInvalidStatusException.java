package nl.kubebit.core.entities.template.exception;

import nl.kubebit.core.entities.common.exception.EntityInvalidStatusException;
import nl.kubebit.core.entities.template.TemplateStatus;

/**
 * 
 */
public class TemplateInvalidStatusException extends EntityInvalidStatusException {

    public TemplateInvalidStatusException(TemplateStatus status) {
        super("Template is in a invalid status: " + status);
    }
    
}
