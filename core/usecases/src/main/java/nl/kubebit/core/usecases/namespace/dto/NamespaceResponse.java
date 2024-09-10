package nl.kubebit.core.usecases.namespace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.kubebit.core.entities.namespace.Namespace;

/**
 * 
 */
public record NamespaceResponse(

    @JsonProperty("id")
    String id,

    @JsonProperty("name")
    String name,

    @JsonProperty("description")
    String description,

    @JsonProperty("is_default")
    boolean isDefault,

    @JsonProperty("is_production")
    boolean isProduction

) {
    public NamespaceResponse(Namespace entity) {
        this(
            entity.id(),
            entity.name(),            
            entity.description(),
            entity.isDefault(),
            entity.isProduction());
    }
}
