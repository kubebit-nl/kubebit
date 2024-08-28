package nl.kubebit.core.infrastructure.resource.datasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.DaemonSet;
import io.fabric8.kubernetes.client.utils.PodStatusUtil;

import nl.kubebit.core.entities.resource.Resource;
import nl.kubebit.core.entities.resource.ResourceContainer;
import nl.kubebit.core.entities.resource.ResourceContainerStatus;
import nl.kubebit.core.entities.resource.ResourcePod;
import nl.kubebit.core.entities.resource.ResourcePort;

/**
 * 
 */
public abstract class ResourceMapper {
    // --------------------------------------------------------------------------------------------

    //
    private static final Logger log = LoggerFactory.getLogger(ResourceMapper.class);

    /**
     *
     */
    public static Resource toEntity(GenericKubernetesResource resource, ResourceRepository repository) {

        //
        String type = null;
        Map<String, String> data = null;
        List<ResourcePort> ports = null;
        Integer replicas = null;
        List<ResourcePod> pods = null;

        // ------------------------------
        // Secret        
        if(Secret.class.getSimpleName().equals(resource.getKind())) {
            var secret = toClass(resource, Secret.class);
            type = secret.map(Secret::getType).orElse(null);
            data = secret.map(Secret::getData).orElse(null);
        }

        // ------------------------------
        // ConfigMap
        if(ConfigMap.class.getSimpleName().equals(resource.getKind())) {
            data = toClass(resource, ConfigMap.class).map(ConfigMap::getData).orElse(null);
        }

        // ------------------------------
        // Service
        if(Service.class.getSimpleName().equals(resource.getKind())) {
            var service = toClass(resource, Service.class);
            ports = service.map(s -> s.getSpec().getPorts()).orElse(List.of()).stream().map(ResourceMapper::toPort).toList();
            ports =  ports.isEmpty() ? null : ports;
        }

        // ------------------------------
        // StatefulSet
        if(Deployment.class.getSimpleName().equals(resource.getKind())) {
            var deployment = toClass(resource, Deployment.class);
            replicas = deployment.map(s -> s.getSpec().getReplicas()).orElse(null);
            pods = deployment.map(repository::getPods).orElse(List.of()).stream().map(ResourceMapper::toPod).toList();
            pods = pods.isEmpty() ? null : pods;
        }

        // ------------------------------
        // StatefulSet
        if(StatefulSet.class.getSimpleName().equals(resource.getKind())) {
            var statefulset = toClass(resource, StatefulSet.class);
            replicas = statefulset.map(s -> s.getSpec().getReplicas()).orElse(null);
            pods = statefulset.map(repository::getPods).orElse(List.of()).stream().map(ResourceMapper::toPod).toList();
            pods = pods.isEmpty() ? null : pods;
        }

        // ------------------------------
        // StatefulSet
        if(DaemonSet.class.getSimpleName().equals(resource.getKind())) {
            var daemonset = toClass(resource, DaemonSet.class);
            pods = daemonset.map(repository::getPods).orElse(List.of()).stream().map(ResourceMapper::toPod).toList();
            pods = pods.isEmpty() ? null : pods;
        }

        //
        return new Resource(
            Resource.extractGroup(resource.getApiVersion()),
            Resource.extractVersion(resource.getApiVersion()),
            resource.getKind(),
            resource.getMetadata().getName(),
            type,
            data,
            ports,
            replicas,
            pods
        );
    }

    // --------------------------------------------------------------------------------------------

    /**
     *
     */
    private static <T> Optional<T> toClass(GenericKubernetesResource resource, Class<T> clazz) {
        try {
            return Optional.of(new ObjectMapper().convertValue(resource, clazz));
        } catch (Exception e) {
            log.error("failed to convert to {} - {}", clazz.getSimpleName(), e.getMessage());
        }
        return Optional.empty();
    }

    // --------------------------------------------------------------------------------------------

    /**
     *
     */
    private static ResourcePort toPort(ServicePort port) {
        return new ResourcePort(
            port.getName(), 
            port.getProtocol(), 
            port.getPort(), 
            port.getTargetPort().toString());
    }

    /**
     *
     */
    private static ResourcePod toPod(Pod pod) {
        var status = PodStatusUtil.getContainerStatus(pod);
        return new ResourcePod(
            pod.getMetadata().getName(), 
            pod.getSpec().getInitContainers().stream().map(c -> ResourceMapper.toContainer(c, status)).toList(),
            pod.getSpec().getContainers().stream().map(c -> ResourceMapper.toContainer(c, status)).toList(),
            pod.getSpec().getNodeName(),
            pod.getStatus().getPhase(),
            pod.getStatus().getStartTime()            
        );
    }

    /**
     *
     */
    private static ResourceContainer toContainer(Container container, List<ContainerStatus> list) {
        var status = list.stream().filter(s -> s.getName().equals(container.getName())).findAny()
            .map(ResourceMapper::toStatus).orElse(null);
        return new ResourceContainer(
            container.getName(),
            container.getImage(),
            container.getEnv().stream().collect(HashMap::new, (m,v) -> m.put(v.getName(), v.getValue()), HashMap::putAll),
            status
        );        
    }

    /**
     *
     */
    private static ResourceContainerStatus toStatus(ContainerStatus status) {

        //
        if(status.getState().getWaiting() != null) {
            return new ResourceContainerStatus(
                status.getName(),
                status.getImage(),
                status.getState().getWaiting().getReason(),
                status.getState().getWaiting().getMessage(),
                false,
                status.getRestartCount()
            );
        }

        //
        if(status.getState().getTerminated() != null) {
            return new ResourceContainerStatus(
                status.getName(),
                status.getImage(),
                status.getState().getTerminated().getReason(),
                status.getState().getTerminated().getMessage(),
                status.getState().getTerminated().getExitCode() == 0,
                status.getRestartCount()
            );
        }

        //
        return new ResourceContainerStatus(
            status.getName(),
            status.getImage(),
            "running",
            "",
            true,
            status.getRestartCount()
        );
    }
       
}
