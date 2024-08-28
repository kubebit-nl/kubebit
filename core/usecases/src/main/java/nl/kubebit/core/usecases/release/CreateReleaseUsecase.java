package nl.kubebit.core.usecases.release;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import nl.kubebit.core.entities.project.exception.ProjectNotCreatedException;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
public interface CreateReleaseUsecase {
    
    //
    ReleaseResponse execute(String projectId, String namespaceName, ReleaseCreateRequest request) throws ProjectNotCreatedException;

    /**
     * 
     */
    record ReleaseCreateRequest(

        @NotBlank
        @Size(min = 2, max = 100)
        @Pattern(regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?")
        @JsonProperty("name")
        String name,

        @NotBlank
        @JsonProperty("template_id")
        String templateId,

        @JsonProperty("values")
        Map<String, Object> values

    ) {
    }

}
