package nl.kubebit.core.usecases.common.event;

import java.io.IOException;

/**
 * 
 */
public interface ServerSideEventGateway {
    
     void sendEvent(String projectId, Object event) throws IOException;

}
