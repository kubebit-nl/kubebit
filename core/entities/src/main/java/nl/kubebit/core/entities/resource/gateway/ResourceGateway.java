package nl.kubebit.core.entities.resource.gateway;

import java.util.Optional;

import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.resource.Resource;

/**
 * 
 */
public interface ResourceGateway {
    
    Optional<Resource> getResource(String namespaceId, ReleaseResourceRef ref);

    Optional<String> getLogs(String namespaceId, String podName, String containerName);

}
