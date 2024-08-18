package nl.kubebit.core.entities.release;

import java.util.Map;

/**
 * 
 */
public record ReleaseRef(

    Long version,
    TemplateRef template,
    Map<String, Object> values
    
) {
    
}
