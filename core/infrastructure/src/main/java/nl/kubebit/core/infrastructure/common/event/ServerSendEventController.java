package nl.kubebit.core.infrastructure.common.event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * ServerSideEventController
 */
@Tag(name = "Events", description = "Server Send Event Controller")
@Profile("sse")
@RestController
public class ServerSendEventController {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ServerSendEventRepository repository;

    /**
     * ServerSideEventController
     *
     * @param repository repository
     */
    public ServerSendEventController(ServerSendEventRepository repository) {
        this.repository = repository;
    }


    /**
     * Subscribe to project channel
     *
     * @param projectId project id
     * @return SseEmitter
     */
    @Operation(summary = "Subscribe to project channel")
    @GetMapping("/api/v1/events/subscribe/{project_id}")
    public SseEmitter subscribe(@PathVariable("project_id") String projectId) {
        return repository.subscribe(projectId);
    }

    /**
     * Subscribe to admin channel
     *
     * @return SseEmitter
     */
    @Operation(summary = "Subscribe to admin channel")
    @GetMapping("/api/admin/v1/events/subscribe")
    public SseEmitter subscribeAdmin() {
        return repository.subscribeAdmin();
    }

    /**
     * Send event to group
     */
    @PostMapping("/events/send/{groupId}")
    public void sendEventToGroup(@PathVariable String groupId, @RequestBody String event) {
        repository.sendEvent(groupId, event);
    }

}
