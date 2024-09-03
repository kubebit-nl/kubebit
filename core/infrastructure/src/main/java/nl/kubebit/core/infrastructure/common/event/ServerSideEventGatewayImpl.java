package nl.kubebit.core.infrastructure.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.usecases.common.annotation.Gateway;
import nl.kubebit.core.usecases.common.event.ServerSideEventGateway;
import org.springframework.context.annotation.Profile;

import static nl.kubebit.core.usecases.common.vars.GlobalVars.SSE_ADMIN;

/**
 * ServerSideEventGateway implementation
 */
@Profile("sse")
@Gateway
public class ServerSideEventGatewayImpl implements ServerSideEventGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ServerSideEventRepository repository;

    /**
     * ServerSideEvent repository
     *
     * @param repository repository
     */
    public ServerSideEventGatewayImpl(ServerSideEventRepository repository) {
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
