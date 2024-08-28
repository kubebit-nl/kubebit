package nl.kubebit.core.usecases.common.event;

/**
 * 
 */
public interface ServerSideEventGateway {
    
     void sendEvent(String projectId, Object event);

}
