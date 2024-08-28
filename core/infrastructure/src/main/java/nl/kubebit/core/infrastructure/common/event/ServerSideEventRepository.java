package nl.kubebit.core.infrastructure.common.event;

import java.io.IOException;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 
 */
@Repository
public class ServerSideEventRepository {
    // --------------------------------------------------------------------------------------------

    /**
     * 
     */
    private final SseEmitter emitter = new SseEmitter(-1L);

    /**
     *
     */
    public SseEmitter getEmitter() {
        return emitter;
    }

    /**
     *
     */
    public void sendEvent(String projectId, String message) {
        try {
  
            //
            emitter.send(SseEmitter.event().data(message));

        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }

}
