package nl.kubebit.core.usecases.release;

import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.Resource;

/**
 * 
 */
public interface GetReleaseManifestUseCase {
    
    Optional<Resource> execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String releaseId,

            @NotNull
            Long revisionVersion);

}
