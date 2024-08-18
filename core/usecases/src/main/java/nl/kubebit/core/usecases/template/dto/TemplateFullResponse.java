package nl.kubebit.core.usecases.template.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.TemplateStatus;

/**
 * 
 */
public record TemplateFullResponse(

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

    @JsonProperty("form")
    Map<String, Object> form,

    @JsonProperty("overlay")
    Map<String, Object> overlay,

    @JsonProperty("app_version")
    String appVersion,

    @JsonProperty("description")
    String description,

    @JsonProperty("keywords")
    List<String> keywords,

    @JsonProperty("schema")
    Map<String, Object> schema

) {
    public TemplateFullResponse(Template entity) {
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
            entity.formSchema(),
            entity.overlayValues(),
            entity.appVersion(),
            entity.description(),
            entity.keywords(),
            entity.chartSchema());
    }
}
