package nl.kubebit.core.infrastructure;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * 
 */
@RestController
public class SseEventController {
    // --------------------------------------------------------------------------------------------

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void startHeartbeat() {
        scheduler.scheduleAtFixedRate(this::sendHeartbeat, 0, 3, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void stopHeartbeat() {
        scheduler.shutdown();
    }

    @GetMapping("/subscribe/{groupId}")
    public SseEmitter subscribe(@PathVariable String groupId) {
        SseEmitter emitter = new SseEmitter();
        emitters.computeIfAbsent(groupId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> emitters.get(groupId).remove(emitter));
        emitter.onTimeout(() -> emitters.get(groupId).remove(emitter));

        return emitter;
    }

    /**
     *
     * @param groupId
     * @param event
     */
    @PostMapping("/send/{groupId}")
    public void sendEventToGroup(@PathVariable String groupId, @RequestBody String event) {
        CopyOnWriteArrayList<SseEmitter> groupEmitters = emitters.get(groupId);
        if (groupEmitters != null) {
            for (SseEmitter emitter : groupEmitters) {
                try {
                    emitter.send(SseEmitter.event().data(event));
                } catch (IOException e) {
                    emitter.complete();
                    //groupEmitters.remove(emitter);
                }
            }
        }
    }

    /**
     *
     */
    private void sendHeartbeat() {
        emitters.forEach((groupId, groupEmitters) -> {
            for (SseEmitter emitter : groupEmitters) {
                try {
                    emitter.send(SseEmitter.event().name("heartbeat").data("heartbeat"));
                } catch (IOException e) {
                    emitter.complete();
                    //groupEmitters.remove(emitter);
                }
            }
        });
    }

    // //
    // private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    // /**
    // *
    // * @return
    // */
    // @GetMapping("/events")
    // public SseEmitter eventStream() {
    // SseEmitter sseEmitter = new SseEmitter();
    // clients.add(sseEmitter);

    // sseEmitter.onTimeout(() -> clients.remove(sseEmitter));
    // sseEmitter.onError(throwable -> clients.remove(sseEmitter));

    // return sseEmitter;
    // }

    // @Async
    // @EventListener
    // public void eventHandler(InnerEvent event) {
    // List<SseEmitter> errorEmitters = new ArrayList<>();

    // clients.forEach(emitter -> {
    // try {
    // emitter.send(event.event(), MediaType.APPLICATION_JSON);
    // } catch (Exception e) {
    // errorEmitters.add(emitter);
    // }
    // });

    // errorEmitters.forEach(clients::remove);
    // }

}
