package nl.kubebit.core.usecases.release;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
public interface RollbackReleaseRevisionUseCase {
    
    ReleaseResponse execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String releaseId,

            @NotNull
            Long revisionVersion);

}
