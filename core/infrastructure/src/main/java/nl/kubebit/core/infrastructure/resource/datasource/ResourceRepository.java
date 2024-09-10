package nl.kubebit.core.infrastructure.resource.datasource;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.fabric8.kubernetes.client.dsl.Deletable;
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
 * api/resources?group=&version=v1&kind=Secret&id=mysql&namespace=default
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
     * Constructor
     *
     * @param kubernetes the kubernetes client
     */
    public ResourceRepository(KubernetesClient kubernetes) {
        this.kubernetes = kubernetes;
    }

    /**
     * Get the resource
     *
     * @param namespace  the namespace
     * @param apiVersion the api version
     * @param kind       the kind
     * @param name       the id
     * @return the resource
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
     * Get the logs
     *
     * @param namespace     the namespace
     * @param podName       the pod id
     * @param containerName the container id
     * @return the logs
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

    /**
     * Remove the resource
     *
     * @param namespace  the namespace
     * @param apiVersion the api version
     * @param kind       the kind
     * @param name       the id
     */
    public void removeResource(@NotBlank String namespace,
                               @NotBlank String apiVersion,
                               @NotBlank String kind,
                               @NotBlank String name) {
        log.info("{} -> removing resource: {} -> {}/{}", namespace, apiVersion, kind, name);
        Optional.ofNullable(
                kubernetes.genericKubernetesResources(apiVersion, kind)
                        .inNamespace(namespace)
                        .withName(name)).ifPresentOrElse(
                Deletable::delete,
                () -> log.warn("resource not found: {}/{}", kind, name));
    }

    // --------------------------------------------------------------------------------------------
    // Private methods

    /**
     *
     */
    public List<Pod> getPods(StatefulSet statefulset) {
        return getPods(statefulset.getMetadata().getNamespace(), statefulset.getSpec().getTemplate().getMetadata().getLabels());
    }

    /**
     *
     */
    public List<Pod> getPods(Deployment deployment) {
        return getPods(deployment.getMetadata().getNamespace(), deployment.getSpec().getTemplate().getMetadata().getLabels());
    }

    /**
     *
     */
    public List<Pod> getPods(DaemonSet daemonSet) {
        return getPods(daemonSet.getMetadata().getNamespace(), daemonSet.getSpec().getTemplate().getMetadata().getLabels());
    }

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
    }

}
