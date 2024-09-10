package nl.kubebit.core.infrastructure.project.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public record ProjectCRDSpec(
    // --------------------------------------------------------------------------------------------

    @JsonProperty("id")
    String name,

    @JsonProperty("description")
    String description
    
) {
}