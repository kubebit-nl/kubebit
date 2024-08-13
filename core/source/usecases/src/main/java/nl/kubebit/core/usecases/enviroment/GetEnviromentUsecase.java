package nl.kubebit.core.usecases.enviroment;

import nl.kubebit.core.entities.enviroment.exception.EnviromentNotFoundException;
import nl.kubebit.core.usecases.enviroment.dto.EnviromentResponse;

/**
 * 
 */
public interface GetEnviromentUsecase {

    EnviromentResponse execute(String projectId, String enviromentId) throws EnviromentNotFoundException;
    
}
