package nl.kubebit.core.usecases.release.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.TemplateRef;

import java.util.List;
import java.util.Map;

/**
 *
 */
public record ReleaseItemResponse(

    @JsonProperty("id")
    String id,

    @JsonProperty("version")
    Long version,

    @JsonProperty("template")
    TemplateRef template,

    @JsonProperty("values")
    Map<String, Object> values,

    @JsonProperty("resources")
    List<ReleaseResourceRef> resources,

    @JsonProperty("icon")
    String icon,

    @JsonProperty("status")
    ReleaseStatus status,

    @JsonProperty("message")
    String message

) {
    public ReleaseItemResponse(Release entity) {
        this(
            entity.id(),
            entity.version(),
            entity.template(),
            entity.values(),
            entity.resources(),
            entity.icon(),
            entity.status(),
            entity.message());
    }
}
