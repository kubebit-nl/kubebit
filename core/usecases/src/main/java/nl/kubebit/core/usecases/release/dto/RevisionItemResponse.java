package nl.kubebit.core.usecases.release.dto;

import nl.kubebit.core.entities.release.ReleaseRef;
import nl.kubebit.core.entities.release.TemplateRef;

import java.util.Map;

/**
 *
 */
public record RevisionItemResponse(

        Long version,
        TemplateRef template,
        Map<String, Object> values

) {
    public RevisionItemResponse(ReleaseRef entity) {
        this(
                entity.version(),
                entity.template(),
                entity.values());
    }
}
