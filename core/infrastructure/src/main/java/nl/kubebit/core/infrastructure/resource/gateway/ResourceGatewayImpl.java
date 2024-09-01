package nl.kubebit.core.infrastructure.resource.gateway;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.resource.Resource;
import nl.kubebit.core.entities.resource.gateway.ResourceGateway;
import nl.kubebit.core.infrastructure.resource.datasource.ResourceMapper;
import nl.kubebit.core.infrastructure.resource.datasource.ResourceRepository;
import nl.kubebit.core.usecases.common.annotation.Gateway;

/**
 *
 */
@Gateway
public class ResourceGatewayImpl implements ResourceGateway {
    // --------------------------------------------------------------------------------------------

    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ResourceRepository repository;

    /**
     * Constructor
     *
     * @param repository the resource repository
     */
    public ResourceGatewayImpl(ResourceRepository repository) {
        this.repository = repository;
    }

    /**
     * Get the resource
     *
     * @param namespaceId the namespace id
     * @param ref         the release resource reference
     * @return the resource
     */
    @Override
    public Optional<Resource> getResource(String namespaceId, ReleaseResourceRef ref) {
        log.debug("{} -> get resource: {}", namespaceId, ref);
        return repository.getResource(
                        namespaceId,
                        ref.apiVersion(),
                        ref.kind(),
                        ref.name())
                .map(resource -> ResourceMapper.toEntity(resource, repository));
    }

    /**
     * Get the logs
     *
     * @param namespaceId   the namespace id
     * @param podName       the pod name
     * @param containerName the container name
     * @return the logs
     */
    @Override
    public Optional<String> getLogs(String namespaceId, String podName, String containerName) {
        log.debug("{} -> get logs: {}/{}", namespaceId, podName, containerName);
        return repository.getLogs(namespaceId, podName, containerName);
    }

    /**
     * Remove the resource from kubernetes
     *
     * @param namespaceId the namespace id
     * @param ref         the release resource reference
     */
    @Override
    public void removeResource(String namespaceId, ReleaseResourceRef ref) {
        log.debug("{} -> remove resource: {}", namespaceId, ref);
        repository.removeResource(
                namespaceId,
                ref.apiVersion(),
                ref.kind(),
                ref.name());
    }

}
