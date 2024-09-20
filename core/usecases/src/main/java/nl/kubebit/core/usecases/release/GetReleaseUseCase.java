package nl.kubebit.core.usecases.release;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.usecases.release.dto.ReleaseItemResponse;

/**
 *
 */
public interface GetReleaseUseCase {

    ReleaseItemResponse execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String releaseId);

}
