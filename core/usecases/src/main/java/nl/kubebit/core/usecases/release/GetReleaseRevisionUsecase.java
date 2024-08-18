package nl.kubebit.core.usecases.release;

import java.util.Map;

import nl.kubebit.core.entities.release.ReleaseRef;
import nl.kubebit.core.entities.release.TemplateRef;

/**
 * 
 */
public interface GetReleaseRevisionUsecase {
    
    //
    ReleaseRefValuesResponse execute(String projectId, String enviromentName, String releaseId, Long revisionVersion);

    /**
     * 
     */
    public record ReleaseRefValuesResponse(

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
