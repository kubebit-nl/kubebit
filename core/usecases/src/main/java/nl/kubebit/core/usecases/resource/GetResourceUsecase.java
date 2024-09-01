package nl.kubebit.core.usecases.resource;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.resource.Resource;

/**
 * 
 */
public interface GetResourceUsecase {
    
    Resource execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotNull
            ReleaseResourceRef ref);
    
}
