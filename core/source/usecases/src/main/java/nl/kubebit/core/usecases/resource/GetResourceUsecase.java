package nl.kubebit.core.usecases.resource;

import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.resource.Resource;

/**
 * 
 */
public interface GetResourceUsecase {
    
    public Resource execute(String projectId, String enviromentName, ReleaseResourceRef ref);
    
}
