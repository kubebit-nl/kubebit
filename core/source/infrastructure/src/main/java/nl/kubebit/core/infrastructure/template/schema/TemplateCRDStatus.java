package nl.kubebit.core.infrastructure.template.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.fabric8.kubernetes.api.model.AnyType;
import nl.kubebit.core.entities.template.TemplateStatus;

/**
 * 
 */
public record TemplateCRDStatus(

    @JsonProperty("status")
    TemplateStatus status,

    @JsonProperty("message")
    String message,
    
    @JsonProperty("schema")
    AnyType schema,

    @JsonProperty("app_version")
    String appVersion,

    @JsonProperty("description")
    String description,    

    @JsonProperty("keywords")
    List<String> keywords

) {

}
