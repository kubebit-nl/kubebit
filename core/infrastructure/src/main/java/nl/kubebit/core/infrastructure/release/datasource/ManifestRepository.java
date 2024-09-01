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
    private final String LABEL_PROJECT = "kubebit.nl/projects";
    private final String LABEL_RELEASE = "kubebit.nl/releases";
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
     * Apply the manifest on kubernetes
     *
     * @param manifestFile manifest file
     * @return list of resources
     * @throws IOException error reading manifest file
     */
    public List<ReleaseResourceRef> applyManifest(File manifestFile) throws IOException {
        log.trace("applying manifest: {}", manifestFile);
        List<ReleaseResourceRef> resources = new ArrayList<>();
        try (var reader = new FileInputStream(manifestFile)) {
            var items = kubernetes.load(reader).items();
            items.forEach(item -> resources.add(createOrPatch(item)));
        }
        return resources;
    }

    /**
     * Get the resources from the manifest file
     *
     * @param manifestFile manifest file
     * @return list of resources
     */
    public List<ReleaseResourceRef> getResources(File manifestFile) throws IOException {
        log.trace("getting resources: {}", manifestFile);
        List<ReleaseResourceRef> resources = new ArrayList<>();
        try (var reader = new FileInputStream(manifestFile)) {
            kubernetes.load(reader).items().forEach(item ->
                    resources.add(new ReleaseResourceRef(item.getApiVersion(), item.getKind(), item.getMetadata().getName())));
        }
        return resources;
    }

    /**
     * Create manifest from input stream
     *
     * @param inputStream    input stream form helm template
     * @param projectId      project id
     * @param releaseId      release id
     * @param releaseVersion release version
     * @param targetFile     manifest file destination
     */
    public void createManifest(InputStream inputStream, String projectId, String releaseId, String releaseVersion, File targetFile) throws IOException {
        log.trace("creating manifest: {}", targetFile.getAbsolutePath());
        try (var writer = new BufferedWriter(new FileWriter(targetFile))) {
            writer.write("# kubebit -> version: " + releaseVersion + "\n");
            var items = kubernetes.load(inputStream).items();
            items.forEach(item -> {
                log.trace("-> {} {}", item.getKind(), item.getMetadata().getName());

                // add labels
                item.getMetadata().getLabels().put(LABEL_MANAGEDBY, "kubebit");
                item.getMetadata().getLabels().put(LABEL_PROJECT, projectId);
                item.getMetadata().getLabels().put(LABEL_RELEASE, releaseId);
                item.getMetadata().getLabels().put(LABEL_VERSION, releaseVersion);

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

    // --------------------------------------------------------------------------------------------

    /**
     * Create or patch the resource on kubernetes
     *
     * @param entity resource to create or patch
     * @param <T>    resource type
     * @return resource reference
     */
    private <T extends HasMetadata> ReleaseResourceRef createOrPatch(T entity) {
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
            resource.forceConflicts().serverSideApply();
        }
        return new ReleaseResourceRef(entity.getApiVersion(), entity.getKind(), entity.getMetadata().getName());
    }


}
