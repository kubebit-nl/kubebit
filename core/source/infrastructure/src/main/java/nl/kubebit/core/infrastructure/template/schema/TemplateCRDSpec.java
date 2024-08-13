package nl.kubebit.core.infrastructure.template.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public record TemplateCRDSpec(
    // --------------------------------------------------------------------------------------------

    @JsonProperty("chart")
    String chart,

    @JsonProperty("version")
    String version,

    @JsonProperty("repository")
    String repository,

    @JsonProperty("type")
    String type,

    @JsonProperty("category")
    String category,

    @JsonProperty("icon")
    String icon
    
) {

}