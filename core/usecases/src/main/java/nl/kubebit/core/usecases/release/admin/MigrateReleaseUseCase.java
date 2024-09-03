package nl.kubebit.core.usecases.release.admin;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.entities.common.exception.EntityNotFoundException;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
public interface MigrateReleaseUseCase {
    
    //
    ReleaseResponse execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String deploymentName

    ) throws EntityNotFoundException;

}
