package nl.kubebit.core.usecases.release;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.usecases.release.dto.RevisionItemResponse;

/**
 * 
 */
public interface GetReleaseRevisionUseCase {
    
    //
    RevisionItemResponse execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String releaseId,

            @NotNull
            Long revisionVersion);

}
