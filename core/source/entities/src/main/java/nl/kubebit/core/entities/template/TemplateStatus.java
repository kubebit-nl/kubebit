package nl.kubebit.core.entities.template;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public enum TemplateStatus {

    @JsonProperty("unknown")
    UNKNOWN,

    @JsonProperty("validating")
    VALIDATING,

    @JsonProperty("unvalid")
    UNVALID,

    @JsonProperty("validated")
    VALIDATED,

    @JsonProperty("deprecated")
    DEPRECATED

}
