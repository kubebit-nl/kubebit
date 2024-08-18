package nl.kubebit.core.usecases.release;

import java.util.Optional;

import org.springframework.core.io.Resource;

/**
 * 
 */
public interface GetReleaseManifestUsecase {
    
    Optional<Resource> execute(String projectId, String enviromentName, String releaseId, Long revisionVersion);

}
