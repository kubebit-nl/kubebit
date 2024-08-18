package nl.kubebit.core.infrastructure.release.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.release.ReleaseStatus;

/**
 * 
 */
public record ReleaseCRDStatus(
    // --------------------------------------------------------------------------------------------

    @JsonProperty(value = "icon")
    String icon,
    
    @JsonProperty(value = "status")
    ReleaseStatus status,

    @JsonProperty(value = "message")
    String message,

    @JsonProperty(value = "resources")
    List<ReleaseResourceRef> resources
    
) {
}
