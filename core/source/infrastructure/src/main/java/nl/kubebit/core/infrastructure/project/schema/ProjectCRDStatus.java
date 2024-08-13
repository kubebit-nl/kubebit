package nl.kubebit.core.infrastructure.project.schema;

/**
 * 
 */
public record ProjectCRDStatus(

    boolean error,
    String message
    
) {
}
