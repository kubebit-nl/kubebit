package nl.kubebit.core.entities.template.exception;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;

/**
 * 
 */
public class ChartFileNotFoundException extends EntityNotFoundException {

    public ChartFileNotFoundException(String fileName) {
        super("Chart file '" + fileName + "' not found");
    }
    
}