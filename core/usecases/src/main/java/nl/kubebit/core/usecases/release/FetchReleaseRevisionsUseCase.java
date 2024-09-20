package nl.kubebit.core.usecases.release;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.usecases.release.dto.RevisionResponse;

/**
 * 
 */
public interface FetchReleaseRevisionsUseCase {
    
    List<RevisionResponse> execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String releaseId

    );
    
}
