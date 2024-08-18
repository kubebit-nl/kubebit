package nl.kubebit.core.usecases.template.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.TemplateStatus;

/**
 * 
 */
public record TemplateResponse(

    @JsonProperty("id")
    String id,

    @JsonProperty("chart")
    String chart,

    @JsonProperty("version")
    String version,

    @JsonProperty("repository")
    String repository,

    @JsonProperty("status")
    TemplateStatus status,

    @JsonProperty("message")
    String message,

    @JsonProperty("type")
    String type,

    @JsonProperty("category")
    String category,

    @JsonProperty("icon")
    String icon,

    @JsonProperty("app_version")
    String appVersion,

    @JsonProperty("description")
    String description,

    @JsonProperty("keywords")
    List<String> keywords

) {
    public TemplateResponse(Template entity) {
        this(
            entity.id(),
            entity.chart(),
            entity.version(),
            entity.repository(),
            entity.status(),
            entity.message(),
            entity.type(),
            entity.category(),
            entity.icon(),
            entity.appVersion(),
            entity.description(),
            entity.keywords());
    }
}
