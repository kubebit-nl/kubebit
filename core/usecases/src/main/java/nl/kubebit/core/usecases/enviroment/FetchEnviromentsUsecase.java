package nl.kubebit.core.usecases.enviroment;

import java.util.List;

import nl.kubebit.core.usecases.enviroment.dto.EnviromentResponse;

/**
 * 
 */
public interface FetchEnviromentsUsecase {

    List<EnviromentResponse> execute(String projectId);
    
}
