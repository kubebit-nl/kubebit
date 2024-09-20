package nl.kubebit.core.usecases.release.dto;

import nl.kubebit.core.entities.release.ReleaseRef;
import nl.kubebit.core.entities.release.TemplateRef;

/**
 * 
 */
public record RevisionResponse(

        Long version,
        TemplateRef template

    ) {
        public RevisionResponse(ReleaseRef entity) {
            this(
                entity.version(), 
                entity.template());
        }
    }
