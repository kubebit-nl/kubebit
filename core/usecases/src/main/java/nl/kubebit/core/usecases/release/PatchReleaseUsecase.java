package nl.kubebit.core.usecases.release;

import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
public interface PatchReleaseUsecase {
    
    ReleaseResponse execute(String projectId, String namespaceName, String releaseId);

}
