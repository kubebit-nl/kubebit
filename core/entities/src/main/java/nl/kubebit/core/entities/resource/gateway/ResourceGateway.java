package nl.kubebit.core.entities.resource.gateway;

import java.util.Optional;

import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.resource.Resource;

/**
 *
 */
public interface ResourceGateway {

    /**
     * Get the resource
     *
     * @param namespaceId the namespace id
     * @param ref         the release resource reference
     * @return the resource
     */
    Optional<Resource> getResource(String namespaceId, ReleaseResourceRef ref);

    /**
     * Get the logs
     *
     * @param namespaceId   the namespace id
     * @param podName       the pod name
     * @param containerName the container name
     * @return the logs
     */
    Optional<String> getLogs(String namespaceId, String podName, String containerName);

    /**
     * Remove the resource
     *
     * @param namespaceId the namespace id
     * @param ref         the release resource reference
     */
    void removeResource(String namespaceId, ReleaseResourceRef ref);

}
