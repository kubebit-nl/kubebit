package nl.kubebit.core.infrastructure.template.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.fabric8.kubernetes.api.model.AnyType;

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

    // ----

    @JsonProperty("type")
    String type,

    @JsonProperty("category")
    String category,

    @JsonProperty("icon")
    String icon,

    // ----

    @JsonProperty("form_schema")
    AnyType formSchema,

    @JsonProperty("overlay_values")
    AnyType overlayValues
    
) {

}