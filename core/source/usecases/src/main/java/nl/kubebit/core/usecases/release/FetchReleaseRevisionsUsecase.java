package nl.kubebit.core.usecases.release;

import java.util.List;

import nl.kubebit.core.usecases.release.dto.ReleaseRefResponse;

/**
 * 
 */
public interface FetchReleaseRevisionsUsecase {
    
    List<ReleaseRefResponse> execute(String projectId, String enviromentName, String releaseId);
    
}
