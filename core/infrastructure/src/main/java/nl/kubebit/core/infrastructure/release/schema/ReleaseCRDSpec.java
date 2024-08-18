package nl.kubebit.core.infrastructure.release.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.fabric8.kubernetes.api.model.AnyType;
import nl.kubebit.core.entities.release.TemplateRef;

/**
 * 
 */
public record ReleaseCRDSpec(
    // --------------------------------------------------------------------------------------------

    @JsonProperty("version")
    Long version,

    @JsonProperty("template")
    TemplateRef template,

    @JsonProperty(value = "values")
    AnyType values

) {
    

}