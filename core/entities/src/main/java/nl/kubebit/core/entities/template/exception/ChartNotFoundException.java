package nl.kubebit.core.entities.template.exception;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;

/**
 * 
 */
public class ChartNotFoundException extends EntityNotFoundException {

    public ChartNotFoundException() {
        super("Chart not found");
    }
    
}
