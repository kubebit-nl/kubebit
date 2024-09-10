package nl.kubebit.core.infrastructure.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.usecases.common.annotation.Gateway;
import nl.kubebit.core.usecases.common.event.ServerSendEventGateway;
import org.springframework.context.annotation.Profile;

import static nl.kubebit.core.entities.common.vars.GlobalVars.SSE_ADMIN;

/**
 * ServerSideEventGateway implementation
 */
@Profile("sse")
@Gateway
public class ServerSendEventGatewayImpl implements ServerSendEventGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ServerSendEventRepository repository;

    /**
     * ServerSideEvent repository
     *
     * @param repository repository
     */
    public ServerSendEventGatewayImpl(ServerSendEventRepository repository) {
        this.repository = repository;
    }

    /**
     * Send event to admin group
     *
     * @param event event
     */
    @Override
    public void sendEvent(Object event) {
        log.debug("send event -> {}", event.getClass().getName());
        repository.sendEvent(SSE_ADMIN, event);
    }

    /**
     * Send event to group
     */
    @Override
    public void sendEvent(String projectId, Object event) {
        log.debug("send event -> {} - {}", projectId, event.getClass().getName());
        repository.sendEvent(projectId, event);
    }

}
