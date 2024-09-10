package nl.kubebit.core.entities.resource;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Resource(

    @JsonProperty(value = "group")
    String group,

    @JsonProperty(value = "version")
    String version,

    @JsonProperty(value = "kind")
    String kind,

    @JsonProperty(value = "id")
    String name,

    // ------------------------------

    // Secret
    @JsonProperty(value = "type")
    String type,

    // Secret, ConfigMap
    @JsonProperty(value = "data")
    Map<String, String> data,

    // Service
    @JsonProperty(value = "ports")
    List<ResourcePort> ports,

    // StatefulSet
    @JsonProperty(value = "replicas")
    Integer replicas,

    // StatefulSet
    @JsonProperty(value = "pods")
    List<ResourcePod> pods

) {

    /**
     *
     */
    public static String extractGroup(String apiVersion) {
        if(apiVersion.contains("/")) {
            return apiVersion.substring(0, apiVersion.indexOf("/"));
        }
        return "";
    }

    /**
     *
     */
    public static String extractVersion(String apiVersion) {
        if(apiVersion.contains("/")) {
            return apiVersion.substring(apiVersion.indexOf("/") + 1);
        }
        return apiVersion;
    }
    
}
