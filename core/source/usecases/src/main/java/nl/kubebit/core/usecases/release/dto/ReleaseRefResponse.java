package nl.kubebit.core.usecases.release.dto;

import nl.kubebit.core.entities.release.ReleaseRef;
import nl.kubebit.core.entities.release.TemplateRef;

/**
 * 
 */
public record ReleaseRefResponse(

        Long version,
        TemplateRef template

    ) {
        public ReleaseRefResponse(ReleaseRef entity) {
            this(
                entity.version(), 
                entity.template());
        }
    }
