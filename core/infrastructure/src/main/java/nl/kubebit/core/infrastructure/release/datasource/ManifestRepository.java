package nl.kubebit.core.infrastructure.release.datasource;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.extensions.NetworkPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import nl.kubebit.core.entities.release.ReleaseResourceRef;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NamespaceableResource;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.fabric8.kubernetes.api.model.HasMetadata;

import static nl.kubebit.core.entities.common.vars.GlobalVars.SYSTEM_NAME;

/**
 *
 */
@Repository
public class ManifestRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final String LABEL_MANAGEDBY = "app.kubernetes.io/managed-by";
    private final String LABEL_PROJECT = "kubebit.nl/project";
    private final String LABEL_VERSION = "kubebit.nl/version";

    //
    private final KubernetesClient kubernetes;

    /**
     * Constructor
     *
     * @param kubernetes kubernetes client
     */
    public ManifestRepository(KubernetesClient kubernetes) {
        this.kubernetes = kubernetes;
    }

    /**
     * Create manifest from input stream
     *
     * @param inputStream    input-stream from helm template
     * @param projectId      project id
     * @param releaseVersion release version
     * @param targetFile     manifest file destination
     */
    public void createManifest(InputStream inputStream, String projectId, Long releaseVersion, File targetFile) throws IOException {
        log.trace("creating manifest: {}", targetFile.getAbsolutePath());
        try (var writer = new BufferedWriter(new FileWriter(targetFile))) {
            writer.write("# " + SYSTEM_NAME + " -> version: " + releaseVersion + "\n");
            var items = kubernetes.load(inputStream).items();
            items.forEach(item -> {
                log.trace("-> {} {}", item.getKind(), item.getMetadata().getName());

                // add labels
                item.getMetadata().getLabels().put(LABEL_MANAGEDBY, SYSTEM_NAME);
                item.getMetadata().getLabels().put(LABEL_PROJECT, projectId);
                item.getMetadata().getLabels().put(LABEL_VERSION, releaseVersion.toString());

                // remove managed by label
                switch (item) {
                    case Deployment deployment ->
                            deployment.getSpec().getTemplate().getMetadata().getLabels().remove(LABEL_MANAGEDBY);
                    case StatefulSet statefulSet ->
                            statefulSet.getSpec().getTemplate().getMetadata().getLabels().remove(LABEL_MANAGEDBY);
                    case NetworkPolicy networkPolicy ->
                            networkPolicy.getSpec().getPodSelector().getMatchLabels().remove(LABEL_MANAGEDBY);
                    default -> {
                    }
                }

                // write item to manifest
                try {
                    writer.write(Serialization.asYaml(item));
                } catch (IOException e) {
                    log.error("error writing item to manifest: {}", item.getKind(), e);
                    throw new RuntimeException(e.getMessage());
                }
            });
        }
        log.trace("manifest created");
    }

    /**
     * Get the resources from the manifest file
     *
     * @param manifestFile manifest file
     * @return list of resources
     */
    public List<ReleaseResourceRef> getResources(File manifestFile) throws IOException, RuntimeException {
        log.trace("getting resources: {}", manifestFile);
        List<ReleaseResourceRef> resources = new ArrayList<>();
        try (var reader = new FileInputStream(manifestFile)) {
            kubernetes.load(reader).items().forEach(item -> resources.add(new ReleaseResourceRef(item.getApiVersion(), item.getKind(), item.getMetadata().getName())));
        }
        if (resources.isEmpty()) {
            throw new RuntimeException("no resources found in manifest");
        }
        return resources;
    }

    /**
     * Apply the manifest on kubernetes
     *
     * @param manifestFile manifest file
     * @throws IOException error reading manifest file
     */
    public void applyManifest(File manifestFile) throws IOException {
        log.trace("applying manifest: {}", manifestFile);
        try (var reader = new FileInputStream(manifestFile)) {
            var items = kubernetes.load(reader).items();
            items.forEach(this::createOrPatch);
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Create or patch the resource on kubernetes
     *
     * @param entity resource to create or patch
     * @param <T>    resource type
     */
    private <T extends HasMetadata> void createOrPatch(T entity) {
        log.trace("apply: {}/{}", entity.getKind(), entity.getMetadata().getName());
        String annotationName = "kubectl.kubernetes.io/last-applied-configuration";
        if (entity.getMetadata().getAnnotations() == null) {
            entity.getMetadata().setAnnotations(new LinkedHashMap<>());
        }
        entity.getMetadata().getAnnotations().put(annotationName, Serialization.asJson(entity));
        NamespaceableResource<T> resource = kubernetes.resource(entity);
        T serverEntity = resource.get();
        if (serverEntity == null) {
            resource.create();
        } else {
            resource.patch();
        }
    }

}
