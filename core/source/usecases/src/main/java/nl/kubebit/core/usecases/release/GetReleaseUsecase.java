package nl.kubebit.core.usecases.release;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.TemplateRef;

/**
 * 
 */
public interface GetReleaseUsecase {
    
    //
    ReleaseValueResponse execute(String projectId, String enviromentName, String releaseId);

    /**
     * 
     */
    public record ReleaseValueResponse(

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

        @JsonProperty("status")
        ReleaseStatus status,

        @JsonProperty("message")
        String message

    ) {
        public ReleaseValueResponse(Release entity) {
            this(
                entity.id(),
                entity.version(),
                entity.template(),
                entity.values(),
                entity.resources(),
                entity.status(),
                entity.message());
        }
    }
}
