package nl.kubebit.core.infrastructure.enviroment.datasource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.client.KubernetesClient;

import static nl.kubebit.core.infrastructure.enviroment.datasource.EnviromentMapper.LABEL_MANAGEDBY_KEY;
import static nl.kubebit.core.infrastructure.enviroment.datasource.EnviromentMapper.LABEL_MANAGEDBY_VALUE;

import static nl.kubebit.core.infrastructure.enviroment.datasource.EnviromentMapper.LABEL_PROJECT_KEY;

/**
 * 
 */
@Repository
public class EnviromentRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    //
    private final KubernetesClient kubernetes;

    /**
     * 
     * @param kubernetes
     */
    public EnviromentRepository(KubernetesClient kubernetes) {
        this.kubernetes = kubernetes;
    }

    /**
     * 
     * @param id
     * @return
     */
    public List<Namespace> findAll() {
        log.trace("find all enviroments");
        return kubernetes.namespaces().withLabel(
                LABEL_MANAGEDBY_KEY, LABEL_MANAGEDBY_VALUE)
            .list()
            .getItems();
    }

    /**
     * 
     * @param id
     * @return
     */
    public List<Namespace> findAllByProject(String projectId) {
        log.trace("{} -> find all enviroments", projectId);
        return kubernetes.namespaces().withLabels(Map.of(
                LABEL_MANAGEDBY_KEY, LABEL_MANAGEDBY_VALUE,
                LABEL_PROJECT_KEY, projectId))
            .list()
            .getItems();
    }
    
    /**
     * 
     * @param projectId
     * @param environmentId
     * @return
     */
    public Optional<Namespace> findByProjectAndId(String projectId, String environmentId) {
        log.trace("{} -> find enviroment by id: {}", projectId, environmentId);
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
            log.trace("{} -> enviroment '{}' not found -> {}", projectId, environmentId, e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * 
     * @param project
     * @return
     */
    public Optional<Namespace> save(String projectId, Namespace namespace) {
        log.trace("{} -> save enviroment: {}", projectId, namespace.getMetadata().getName());
        try {
            return Optional.of(kubernetes.namespaces().resource(namespace).create());
        } catch (Exception e) {
            log.error("{} -> enviroment not created -> {}", projectId, e.getMessage());
        }
        return Optional.empty();
    }
}
