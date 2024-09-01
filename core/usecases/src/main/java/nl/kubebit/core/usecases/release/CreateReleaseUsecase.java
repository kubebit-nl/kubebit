package nl.kubebit.core.usecases.release;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;
import nl.kubebit.core.entities.release.exception.ReleaseNotCreatedException;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
public interface CreateReleaseUsecase {
    
    //
    ReleaseResponse execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            @Valid
            ReleaseCreateRequest request) throws EntityNotFoundException, ReleaseNotCreatedException;

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
