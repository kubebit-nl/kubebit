package nl.kubebit.core.infrastructure.common.event;

/**
 *
 */
public record ServerSideEvent(

    String type,
    Object content

) {

    /**
     * Constructor
     * @param content content
     */
    public ServerSideEvent(Object content) {
        this(content.getClass().getSimpleName(), content);
    }

}
