package nl.kubebit.core.infrastructure.common.event;

import nl.kubebit.core.usecases.common.annotation.Gateway;
import nl.kubebit.core.usecases.common.event.ServerSideEventGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import static nl.kubebit.core.usecases.common.vars.GlobalVars.SSE_ADMIN;

/**
 * ServerSideEventGateway console implementation for debug purposes
 */
@Profile("!sse")
@Gateway
public class ServerSideEventGatewayConsoleImpl implements ServerSideEventGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Send event to admin group
     *
     * @param event event
     */
    @Override
    public void sendEvent(Object event) {
        log.debug("send event -> {}", event.getClass().getName());
    }

    /**
     * Send event to group
     *
     * @param projectId project id
     * @param event     event
     */
    @Override
    public void sendEvent(String projectId, Object event) {
        log.debug("send event -> {} - {}", projectId, event.getClass().getName());
    }
}
