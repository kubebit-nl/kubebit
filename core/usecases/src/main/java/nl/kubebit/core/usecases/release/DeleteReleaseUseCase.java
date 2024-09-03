package nl.kubebit.core.usecases.release;

import jakarta.validation.constraints.NotBlank;

/**
 * 
 */
public interface DeleteReleaseUseCase {
    
    void execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String releaseId);
    
}
