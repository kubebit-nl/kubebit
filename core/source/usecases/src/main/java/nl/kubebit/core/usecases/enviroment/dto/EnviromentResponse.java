package nl.kubebit.core.usecases.enviroment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.kubebit.core.entities.enviroment.Enviroment;

/**
 * 
 */
public record EnviromentResponse(

    @JsonProperty("id")
    String id,

    @JsonProperty("name")
    String name,

    @JsonProperty("description")
    String description

) {
    public EnviromentResponse(Enviroment entity) {
        this(
            entity.id(),
            entity.name(),            
            entity.description());
    }
}
