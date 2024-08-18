package nl.kubebit.core.infrastructure.release.datasource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseResourceRef;
import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.usecases.common.util.HelmBuilder;
import nl.kubebit.core.usecases.common.util.HelmValuesMerger;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NamespaceableResource;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.networking.v1.NetworkPolicy;

import static nl.kubebit.core.infrastructure.configuration.StorageConfig.MANIFESTS_LOCATION;

/**
 * 
 */
@Repository
public class ManifestRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    public final String LABEL_MANAGEDBY = "app.kubernetes.io/managed-by";
    public final String LABEL_PROJECT = "kubebit.nl/project";
    public final String LABEL_RELEASE = "kubebit.nl/release";
    public final String LABEL_VERSION = "kubebit.nl/version";

    //
    public final String YAML_EXT = ".yaml";

    //
    private final KubernetesClient kubernetes;

    /**
     * 
     * @param kubernetes
     */
    public ManifestRepository(KubernetesClient kubernetes) {
        this.kubernetes = kubernetes;
    }

    /**
     * 
     * @param release
     * @param chartFile
     * @throws IOException 
     * @throws InterruptedException 
     */
    public void patchManifest(String projectId, String enviromentName, Release release) throws IOException, InterruptedException {
        log.debug("{} -> patching chart: {}", release.enviromentId(), release.id());

        //
        var manifestPath = Paths.get(MANIFESTS_LOCATION, projectId, enviromentName, release.id());
        var manifestFile = Paths.get(manifestPath.toString(), release.version() + YAML_EXT).toFile();
        log.trace("manifest file: {}", manifestFile);

        //
        try(var reader = new FileInputStream(manifestFile)) {
            var items = kubernetes.load(reader).items();
            items.stream().forEach(item -> {
                log.trace(item.getKind() + "/" + item.getMetadata().getName());

                // patch manifest
                createOrPatch(item);
            });            
        }
    }


    /**
     * 
     * @param projectId
     * @param enviromentName
     * @param release
     * @param chartFile
     * @return 
     * @throws IOException
     * @throws InterruptedException
     */
    public List<ReleaseResourceRef> installManifest(String projectId, String enviromentName, Release release, Template template, File chartFile) throws IOException, InterruptedException {
        log.debug("{} -> installing chart: {}", release.enviromentId(), chartFile.getName());

        //
        var values = template.chartValues();
        var overlay = template.overlayValues();  

        //
        var valuesFile = Files.createTempFile(release.id() + "-", "-values.yaml");

        // merge values
        HelmValuesMerger.combine(values, overlay, release.values(), valuesFile);

        //
        var process = HelmBuilder.init()
            .template(release.id(), chartFile)
            .namespace(release.enviromentId())
            .values(valuesFile)
            .executeAs();

        //
        var manifestPath = Paths.get(MANIFESTS_LOCATION, projectId, enviromentName, release.id());
        log.trace("manifest path: {}", manifestPath);
        Files.createDirectories(manifestPath);

        //
        cleanRevisions(manifestPath, release);

        //
        var manifestFile = Paths.get(manifestPath.toString(), release.version() + YAML_EXT).toFile();
        log.trace("manifest file: {}", manifestFile);

        //
        List<ReleaseResourceRef> resources = new ArrayList<>();
        try(var writer = new BufferedWriter(new FileWriter(manifestFile))) {
            try (var inputStream = process.getInputStream()) {
                writer.write("# kubebit -> version: " + release.version() + "\n");
                var items = kubernetes.load(inputStream).items();
                items.stream().forEach(item -> {
                    log.debug(item.getKind() + " " + item.getMetadata().getName());

                    //
                    item.getMetadata().getLabels().put(LABEL_MANAGEDBY, "kubebit");
                    item.getMetadata().getLabels().put(LABEL_PROJECT, projectId);
                    item.getMetadata().getLabels().put(LABEL_RELEASE, release.id());
                    item.getMetadata().getLabels().put(LABEL_VERSION, release.version().toString());

                    //
                    if(item instanceof Deployment deployment) {
                        deployment.getSpec().getTemplate().getMetadata().getLabels().remove(LABEL_MANAGEDBY);
                    }
                    else if(item instanceof StatefulSet statefulSet) {
                        statefulSet.getSpec().getTemplate().getMetadata().getLabels().remove(LABEL_MANAGEDBY);
                    }
                    else if(item instanceof NetworkPolicy networkPolicy) {
                        networkPolicy.getSpec().getPodSelector().getMatchLabels().remove(LABEL_MANAGEDBY);
                    }

                    //
                    try {
                        writer.write(Serialization.asYaml(item));
                    } catch (IOException e) {
                        log.error("error writing item to manifest: " + item.getKind(), e);
                    }

                    // create or patch manifest
                    createOrPatch(item);

                    // add resource to list
                    resources.add(
                        new ReleaseResourceRef(
                            item.getApiVersion(), 
                            item.getKind(), 
                            item.getMetadata()
                        .getName()));   
                });
            }
            process.waitFor();
        }
        return resources;
    }

    /**
     * 
     * @param <T>
     * @param inEntity
     * @return
     */
    protected <T extends HasMetadata> T createOrPatch(T inEntity) {
        String annotationName = "kubectl.kubernetes.io/last-applied-configuration";
        if (inEntity.getMetadata().getAnnotations() == null) {
            inEntity.getMetadata().setAnnotations(new LinkedHashMap<>());
        }
        inEntity.getMetadata().getAnnotations().put(annotationName, Serialization.asJson(inEntity));
        NamespaceableResource<T> resource = kubernetes.resource(inEntity);
        T serverEntity = resource.get();
        T outEntity;
        if (serverEntity == null) {
            outEntity = resource.create();
        } else {
            outEntity = resource.forceConflicts().serverSideApply();
        }
        return outEntity;
    }

    /**
     * 
     */
    private void cleanRevisions(Path manifestPath, Release release) {
        try {
            // get revisions and current release
            var filesToKeep = release.revisions().stream()
                .map(revision -> revision.version() + YAML_EXT).collect(Collectors.toList());

            // iterate through the list of files and delete the ones that are not in the list
            for (Path file : Files.list(manifestPath).collect(Collectors.toList())) {
                if(!filesToKeep.contains(file.getFileName().toString())) {
                    Files.delete(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
