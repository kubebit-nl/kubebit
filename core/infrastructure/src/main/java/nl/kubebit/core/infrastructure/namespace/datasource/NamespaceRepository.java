package nl.kubebit.core.infrastructure.namespace.datasource;

import static nl.kubebit.core.infrastructure.namespace.datasource.NamespaceMapper.LABEL_MANAGEDBY_KEY;
import static nl.kubebit.core.infrastructure.namespace.datasource.NamespaceMapper.LABEL_MANAGEDBY_VALUE;
import static nl.kubebit.core.infrastructure.namespace.datasource.NamespaceMapper.LABEL_PROJECT_KEY;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * 
 */
@Repository
public class NamespaceRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    //
    private final KubernetesClient kubernetes;

    /**
     *
     */
    public NamespaceRepository(KubernetesClient kubernetes) {
        this.kubernetes = kubernetes;
    }

    /**
     *
     */
    public List<Namespace> findAll() {
        log.trace("find all namespaces");
        return kubernetes.namespaces().withLabel(
                LABEL_MANAGEDBY_KEY, LABEL_MANAGEDBY_VALUE)
            .list()
            .getItems();
    }

    /**
     *
     */
    public List<Namespace> findAllByProject(String projectId) {
        log.trace("{} -> find all namespaces", projectId);
        return kubernetes.namespaces().withLabels(Map.of(
                LABEL_MANAGEDBY_KEY, LABEL_MANAGEDBY_VALUE,
                LABEL_PROJECT_KEY, projectId))
            .list()
            .getItems();
    }
    
    /**
     *
     */
    public Optional<Namespace> findByProjectAndId(String projectId, String environmentId) {
        log.trace("{} -> find namespace by id: {}", projectId, environmentId);
        try {
            return kubernetes.namespaces()
                .withLabels(Map.of(
                    LABEL_MANAGEDBY_KEY, LABEL_MANAGEDBY_VALUE,
                    LABEL_PROJECT_KEY, projectId
                ))
                .list()
                .getItems().stream()
                    .filter(n -> n.getMetadata().getName().equals(environmentId))
                    .findAny();
        } catch (Exception e) {
            log.trace("{} -> namespace '{}' not found -> {}", projectId, environmentId, e.getMessage());
        }
        return Optional.empty();
    }

    /**
     *
     */
    public Optional<Namespace> save(String projectId, Namespace namespace) {
        log.trace("{} -> save namespace: {}", projectId, namespace.getMetadata().getName());
        try {
            return Optional.of(kubernetes.namespaces().resource(namespace).create());
        } catch (Exception e) {
            log.error("{} -> namespace not created -> {}", projectId, e.getMessage());
        }
        return Optional.empty();
    }
}
