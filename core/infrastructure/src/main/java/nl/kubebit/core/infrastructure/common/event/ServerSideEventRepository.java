package nl.kubebit.core.infrastructure.common.event;

import java.io.IOException;
import java.util.concurrent.*;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static nl.kubebit.core.usecases.common.vars.GlobalVars.SSE_ADMIN;

/**
 * ServerSideEventRepository
 */
@Profile("sse")
@Repository
public class ServerSideEventRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService schedulerHeartbeat = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService schedulerCleaner = Executors.newScheduledThreadPool(1);

    //
    private Integer counter = 0;

    /**
     * Start schedulers
     */
    @PostConstruct
    public void startScheduler() {
        schedulerHeartbeat.scheduleAtFixedRate(this::sendHeartbeat, 0, 3, TimeUnit.SECONDS);
        schedulerCleaner.scheduleAtFixedRate(this::cleanEmptyGroups, 1, 1, TimeUnit.HOURS);
    }

    /**
     * Stop schedulers
     */
    @PreDestroy
    public void stopSchedulers() {
        schedulerHeartbeat.shutdown();
        schedulerCleaner.shutdown();
    }

    /**
     * Display emitters
     */
    public void display() {
        emitters.forEach((key, value) -> {
            log.info("group: {}", key);
            value.forEach(emitter -> log.info("- emitter: {}", emitter));
        });
    }

    /**
     * Subscribe to admin channel
     *
     */
    public SseEmitter subscribeAdmin() {
        return subscribe(SSE_ADMIN);
    }

    /**
     * Subscribe to group channel
     *
     * @param groupId group id
     * @return SseEmitter
     */
    public SseEmitter subscribe(@NotBlank String groupId) {
        log.info("subscribe to group: {}", groupId);
        SseEmitter emitter = new SseEmitter();
        emitters.computeIfAbsent(groupId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        log.trace("emitters: {}", emitters.size());

        emitter.onCompletion(() -> {
            emitters.get(groupId).remove(emitter);
            log.trace("emitter completed: {}", emitters.get(groupId).size());
        });

        emitter.onTimeout(() -> {
            emitter.complete();
            log.trace("emitter timeout");
        });

        emitter.onError((e) -> {
            emitter.completeWithError(e);
            log.trace("emitter error: {}", e.getMessage());
        });

        return emitter;
    }

    /**
     * Send event to group
     */
    public void sendEvent(String groupId, Object object) {
        try {
            CopyOnWriteArrayList<SseEmitter> groupEmitters = emitters.get(groupId);
            if (groupEmitters != null) {

                // build content
                var content = new ServerSideEvent(object);

                // build event object
                var event = SseEmitter.event()
                        .id(getId())
                        .name(groupId)
                        .data(content, MediaType.APPLICATION_JSON)
                        .build();

                // send event to all emitters
                for (SseEmitter emitter : groupEmitters) {
                    try {
                        emitter.send(event);
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("failed to send event", e);
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Send heartbeat
     */
    private void sendHeartbeat() {
        emitters.forEach((groupId, groupEmitters) -> {
            for (SseEmitter emitter : groupEmitters) {
                try {
                    emitter.send(SseEmitter.event().name("heartbeat").data("heartbeat"));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }
        });
    }

    /**
     * Clean empty groups
     */
    private void cleanEmptyGroups() {
        log.info("cleaning emitter groups");
        emitters.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    /**
     * Get next event id
     *
     * @return id
     */
    private String getId() {
        counter++;
        if (counter > 1000) {
            counter = 0;
        }
        return counter.toString();
    }
}
