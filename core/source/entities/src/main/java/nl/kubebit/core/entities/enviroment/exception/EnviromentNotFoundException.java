package nl.kubebit.core.entities.enviroment.exception;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;

/**
 * 
 */
public class EnviromentNotFoundException extends EntityNotFoundException {

    public EnviromentNotFoundException(String enviromentId) {
        super("Enviroment '" + enviromentId + "' not found");
    }
    
}
