package nl.kubebit.core.entities.enviroment.exception;

import nl.kubebit.core.entities.common.exception.EntityNotUpdatedException;

/**
 * 
 */
public class EnviromentNotUpdatedException extends EntityNotUpdatedException {

    public EnviromentNotUpdatedException() {
        super("Enviroment not updated");
    }
    
}
