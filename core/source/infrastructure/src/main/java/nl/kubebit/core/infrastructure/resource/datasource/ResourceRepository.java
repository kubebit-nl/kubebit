package nl.kubebit.core.infrastructure.resource.datasource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;

import jakarta.validation.constraints.NotBlank;

/**
 * api/resources?group=&version=v1&kind=Secret&name=mysql&namespace=default
 */
@Repository
@Validated
public class ResourceRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final KubernetesClient kubernetes;

    /**
     * 
     * @param kubernetes
     */
    public ResourceRepository(KubernetesClient kubernetes) {
        this.kubernetes = kubernetes;
    }

    /**
     * 
     * @param group
     * @param apiVersion
     * @param kind
     * @param name
     * @param namespace
     * @return
     */
    public Optional<GenericKubernetesResource> getResource(
            @NotBlank String namespace,
            @NotBlank String apiVersion,
            @NotBlank String kind,
            @NotBlank String name) {
        try {
            log.info("{} -> get resource: {} -> {}/{}", namespace, apiVersion, kind, name);
            return Optional.ofNullable(
                    kubernetes.genericKubernetesResources(apiVersion, kind)
                            .inNamespace(namespace)
                            .withName(name)
                            .get());
        } catch (Exception e) {
            log.trace("failed to get resource: {}", e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * 
     * @param namespace
     * @param podName
     * @param containerName
     * @return
     */
    public Optional<String> getLogs(String namespace, String podName, String containerName) {
        try {
            return Optional.of(kubernetes.pods()
                .inNamespace(namespace)
                .withName(podName)
                .inContainer(containerName)
                .getLog());
        } catch (Exception e) {
            log.trace("failed to get logs: {}", e.getMessage());
        }
        return Optional.empty();
    }

    // --------------------------------------------------------------------------------------------


    /**
     * 
     */
    public List<Pod> getPods(StatefulSet statefulset) {
        return getPods(statefulset.getMetadata().getNamespace(), statefulset.getSpec().getTemplate().getMetadata().getLabels());
    };

    /**
     * 
     */
    public List<Pod> getPods(Deployment deployment) {
        return getPods(deployment.getMetadata().getNamespace(), deployment.getSpec().getTemplate().getMetadata().getLabels());
    };

    /**
     * 
     */
    public List<Pod> getPods(DaemonSet daemonSet) {
        return getPods(daemonSet.getMetadata().getNamespace(), daemonSet.getSpec().getTemplate().getMetadata().getLabels());
    };

    /**
     * 
     */
    private List<Pod> getPods(String namespace, Map<String, String> labels) {
        try {
            log.info("{} -> getting pods", namespace);

            //
            var selector = new LabelSelector();
            selector.setMatchLabels(labels);

            //
            return kubernetes.pods()
                .inNamespace(namespace)
                .withLabelSelector(selector)
                .list()
                .getItems();

        } catch (Exception e) {
            log.trace("failed to get pods: {}", e.getMessage());
        }
        return List.of();
    };

    

 
}
