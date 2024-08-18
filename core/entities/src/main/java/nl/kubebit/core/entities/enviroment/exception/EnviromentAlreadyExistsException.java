package nl.kubebit.core.entities.enviroment.exception;

import nl.kubebit.core.entities.common.exception.EntityAlreadyExistsException;

/**
 * 
 */
public class EnviromentAlreadyExistsException extends EntityAlreadyExistsException {

    public EnviromentAlreadyExistsException(String enviromentName) {
        super("Enviroment '" + enviromentName + "' already exists");
    }
    
}
