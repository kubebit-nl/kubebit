package nl.kubebit.core.entities.resource.gateway;

import java.util.Optional;

import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.resource.Resource;

/**
 * 
 */
public interface ResourceGateway {
    
    public Optional<Resource> getResource(String enviromentId, ReleaseResourceRef ref);

    public Optional<String> getLogs(String enviromentId, String podName, String containerName);

}
