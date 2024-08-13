package nl.kubebit.core.entities.enviroment.exception;

import nl.kubebit.core.entities.common.exception.EntityNotCreatedException;

/**
 * 
 */
public class EnviromentNotCreatedException extends EntityNotCreatedException {

    public EnviromentNotCreatedException() {
        super("Enviroment not created");
    }
    
}
