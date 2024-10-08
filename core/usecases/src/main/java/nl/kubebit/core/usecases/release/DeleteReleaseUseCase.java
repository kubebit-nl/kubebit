package nl.kubebit.core.usecases.release;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
public interface DeleteReleaseUseCase {

    ReleaseResponse execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String releaseId);
    
}
