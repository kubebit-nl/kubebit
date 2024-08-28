package nl.kubebit.core.entities.release;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.kubebit.core.entities.resource.Resource;

/**
 * 
 */
public record ReleaseResourceRef(

    @JsonProperty(value = "group")
    String group,

    @JsonProperty(value = "version")
    String version,

    @JsonProperty(value = "kind")
    String kind,

    @JsonProperty(value = "name")
    String name
    
) {

    /**
     *
     */
    public ReleaseResourceRef(String apiVersion, String kind, String name) {
        this(
            Resource.extractGroup(apiVersion), 
            Resource.extractVersion(apiVersion), 
            kind, 
            name);
    }

    /**
     *
     */
    public String apiVersion() {
        if(group != null && !group.isEmpty()) {
            return group + "/" + version;
        }
        return version;        
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return group + "/" + version + " -> " + kind + "/" + name;
    }
}
