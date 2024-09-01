package nl.kubebit.core.usecases.release;

import jakarta.validation.constraints.NotBlank;

/**
 * 
 */
public interface DeleteReleaseUsecase {
    
    void execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String releaseId);
    
}
