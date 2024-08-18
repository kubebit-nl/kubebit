package nl.kubebit.core.usecases.release;

import java.util.List;

import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
public interface FetchReleasesUsecase {
    
    List<ReleaseResponse> execute(String projectId, String enviromentName);
}
