package nl.kubebit.core.infrastructure.namespace.datasource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.dsl.FilterWatchListDeletable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.client.KubernetesClient;

import static nl.kubebit.core.infrastructure.namespace.datasource.NamespaceMapper.*;

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
    public List<Namespace> findByProject(String projectId) {
        log.trace("{} -> find all namespaces", projectId);
        return kubernetes.namespaces()
                .withLabel(LABEL_PROJECT_KEY, projectId)
                .withLabel(LABEL_MANAGEDBY_KEY, LABEL_MANAGEDBY_VALUE)
            .list()
            .getItems();
    }

    /**
     *
     * @param projectId
     * @param namespaceId
     * @return
     */
    public Optional<Namespace> findById(String projectId, String namespaceId) {
        log.trace("find namespace by id: {}", namespaceId);
        try {
            return Optional.ofNullable(kubernetes.namespaces().withName(namespaceId).get())
                    .filter(namespace -> projectId.equals(namespace.getMetadata().getLabels().get(LABEL_PROJECT_KEY)))
                    .filter(namespace -> LABEL_MANAGEDBY_VALUE.equals(namespace.getMetadata().getLabels().get(LABEL_MANAGEDBY_KEY)));
        } catch (Exception e) {
            log.trace("namespace '{}' not found -> {}", namespaceId, e.getMessage());
        }
        return Optional.empty();
    }

    /**
     *
     * @param projectId
     * @param namespaceName
     * @return
     */
    public Optional<Namespace> findByName(String projectId, String namespaceName) {
        log.trace("{} -> find namespace by name: {}", projectId, namespaceName);
        try {
            return kubernetes.namespaces()
                    .withLabel(LABEL_PROJECT_KEY, projectId)
                    .withLabel(LABEL_NAME_KEY, namespaceName)
                    .withLabel(LABEL_MANAGEDBY_KEY, LABEL_MANAGEDBY_VALUE)
                    .list().getItems().stream().findAny();
        } catch (Exception e) {
            log.trace("{} -> namespace '{}' not found -> {}", projectId, namespaceName, e.getMessage());
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
