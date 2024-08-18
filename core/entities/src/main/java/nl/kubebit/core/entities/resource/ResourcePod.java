package nl.kubebit.core.entities.resource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public record ResourcePod(
        
    @JsonProperty(value = "name")
    String name,

    @JsonProperty(value = "init_containers")
    List<ResourceContainer> initContainers,

    @JsonProperty(value = "containers")
    List<ResourceContainer> containers,

    @JsonProperty(value = "node")
    String node,

    @JsonProperty(value = "phase")
    String phase,

    @JsonProperty(value = "started")
    String started    

) {
    
}
