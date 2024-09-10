package nl.kubebit.core.usecases.common.event;

/**
 * 
 */
public interface ServerSendEventGateway {

     void sendEvent(Object event);

     void sendEvent(String projectId, Object event);

}
