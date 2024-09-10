package nl.kubebit.core.entities.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public record ResourceContainerStatus(

    @JsonProperty(value = "id")
    String name,

    @JsonProperty(value = "image")
    String image,

    @JsonProperty(value = "status")
    String status,

    @JsonProperty(value = "message")
    String message,

    @JsonProperty(value = "running")
    Boolean running,

    @JsonProperty(value = "restarts")
    Integer restarts

) {
    
}
