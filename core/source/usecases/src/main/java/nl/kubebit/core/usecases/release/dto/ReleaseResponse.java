package nl.kubebit.core.usecases.release.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.TemplateRef;

/**
 * 
 */
public record ReleaseResponse(

    @JsonProperty("id")
    String id,

    @JsonProperty("version")
    Long version,

    @JsonProperty("template")
    TemplateRef template,

    @JsonProperty("status")
    ReleaseStatus status,

    @JsonProperty("message")
    String message

) {
    public ReleaseResponse(Release entity) {
        this(
            entity.id(),
            entity.version(),
            entity.template(),
            entity.status(),
            entity.message());
    }
}
