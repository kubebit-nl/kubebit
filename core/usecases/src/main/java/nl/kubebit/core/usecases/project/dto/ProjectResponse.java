package nl.kubebit.core.usecases.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.kubebit.core.entities.project.Project;

/**
 * 
 */
public record ProjectResponse(

    @JsonProperty("id")
    String id,

    @JsonProperty("name")
    String name,

    @JsonProperty("description")
    String description

) {
    public ProjectResponse(Project entity) {
        this(
            entity.id(), 
            entity.name(), 
            entity.description());
    }
}
