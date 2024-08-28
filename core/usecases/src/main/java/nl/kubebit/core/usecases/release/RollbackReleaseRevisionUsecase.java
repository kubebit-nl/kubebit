package nl.kubebit.core.usecases.release;

import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
public interface RollbackReleaseRevisionUsecase {
    
    ReleaseResponse execute(String projectId, String namespaceName, String releaseId, Long revisionVersion);

}
