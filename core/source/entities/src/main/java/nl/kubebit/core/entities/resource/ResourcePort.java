package nl.kubebit.core.entities.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ResourcePort(
        
    @JsonProperty(value = "name")
    String name,

    @JsonProperty(value = "protocol")
    String protocol,

    @JsonProperty(value = "port")
    Integer port,

    @JsonProperty(value = "targetPort")
    String targetPort

) {
    
}
