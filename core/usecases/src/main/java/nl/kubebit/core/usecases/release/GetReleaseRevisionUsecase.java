package nl.kubebit.core.usecases.release;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.entities.release.ReleaseRef;
import nl.kubebit.core.entities.release.TemplateRef;

/**
 * 
 */
public interface GetReleaseRevisionUsecase {
    
    //
    ReleaseRefValuesResponse execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String releaseId,

            @NotNull
            Long revisionVersion);

    /**
     * 
     */
    record ReleaseRefValuesResponse(

        Long version,
        TemplateRef template,
        Map<String, Object> values

    ) {
        public ReleaseRefValuesResponse(ReleaseRef entity) {
            this(
                entity.version(), 
                entity.template(),
                entity.values());
        }
    }
}
