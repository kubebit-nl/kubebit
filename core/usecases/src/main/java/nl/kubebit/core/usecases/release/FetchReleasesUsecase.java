package nl.kubebit.core.usecases.release;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
public interface FetchReleasesUsecase {
    
    List<ReleaseResponse> execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName

    );
}
