package nl.kubebit.core.infrastructure.common.event;

/**
 *
 */
public record ServerSendEvent(

    String type,
    Object content

) {

    /**
     * Constructor
     * @param content content
     */
    public ServerSendEvent(Object content) {
        this(content.getClass().getSimpleName(), content);
    }

}
