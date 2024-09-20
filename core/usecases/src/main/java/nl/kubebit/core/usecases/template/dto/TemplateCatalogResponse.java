package nl.kubebit.core.usecases.template.dto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.kubebit.core.entities.template.Template;

/**
 * 
 */
public record TemplateCatalogResponse(

    @JsonProperty("id")
    String id,

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
    String icon,

    @JsonProperty("app_version")
    String appVersion,

    @JsonProperty("description")
    String description,

    @JsonProperty("keywords")
    List<String> keywords,

    @JsonProperty("schema")
    Map<String, Object> schema

) {
    public TemplateCatalogResponse(Template entity) {
        this(
            entity.id(),
            entity.chart(),
            entity.version(),
            entity.repository(),
            entity.type(),
            entity.category(),
            entity.icon(),
            entity.appVersion(),
            entity.description(),
            entity.keywords(),
            entity.schema());
    }
}
