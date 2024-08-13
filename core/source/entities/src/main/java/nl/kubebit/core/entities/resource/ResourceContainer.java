package nl.kubebit.core.entities.resource;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public record ResourceContainer(
        
    @JsonProperty(value = "name")
    String name,

    @JsonProperty(value = "image")
    String image,

    @JsonProperty(value = "env")
    Map<String, Object> env,

    @JsonProperty(value = "status")
    ResourceContainerStatus status

) {
    
}
