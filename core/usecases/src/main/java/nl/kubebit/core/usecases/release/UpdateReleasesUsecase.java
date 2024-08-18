package nl.kubebit.core.usecases.release;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
public interface UpdateReleasesUsecase {
    
    ReleaseResponse execute(String projectId, String enviromentName, String releaseId, ReleaseUpdateRequest request);

    /**
     * 
     */
    public record ReleaseUpdateRequest(

        @NotBlank
        @JsonProperty("template_id")
        String templateId,

        @JsonProperty("values")
        Map<String, Object> values

    ) {
    }
}
