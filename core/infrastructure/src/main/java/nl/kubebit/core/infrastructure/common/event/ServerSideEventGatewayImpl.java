package nl.kubebit.core.infrastructure.common.event;

import org.springframework.context.ApplicationEventPublisher;

import nl.kubebit.core.usecases.common.annotation.Gateway;
import nl.kubebit.core.usecases.common.event.ServerSideEventGateway;

/**
 * 
 */
@Gateway
public class ServerSideEventGatewayImpl implements ServerSideEventGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final ApplicationEventPublisher publisher;

    /**
     *
     */
    public ServerSideEventGatewayImpl(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 
     */
    @Override
    public void sendEvent(String projectId, Object event) {
        publisher.publishEvent(new InnerEvent(projectId, event));
    }

    /**
     * 
     */
    public record InnerEvent(
        String projectId,
        Object event
    ) {
    }

}
