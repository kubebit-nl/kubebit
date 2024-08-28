package nl.kubebit.core.usecases.resource;

import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.resource.Resource;

/**
 * 
 */
public interface GetResourceUsecase {
    
    Resource execute(String projectId, String namespaceName, ReleaseResourceRef ref);
    
}
