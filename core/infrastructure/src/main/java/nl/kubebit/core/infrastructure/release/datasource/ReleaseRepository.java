package nl.kubebit.core.infrastructure.release.datasource;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.client.KubernetesClient;
import nl.kubebit.core.infrastructure.release.schema.ReleaseCRD;
import nl.kubebit.core.infrastructure.release.schema.ReleaseCRDSpec;

/**
 * 
 */
@Repository
public class ReleaseRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final KubernetesClient kubernetes;

    /**
     *
     */
    public ReleaseRepository(KubernetesClient kubernetes) {
        this.kubernetes = kubernetes;
    }

    /**
     *
     */
    public List<ReleaseCRD> findAll() {
        log.trace("find all releases");
        return kubernetes.resources(ReleaseCRD.class).list().getItems();
    }

    /**
     *
     */
    public List<ReleaseCRD> findAllInNamespace(String namespace) {
        log.trace("{} -> find all releases", namespace);
        return kubernetes.resources(ReleaseCRD.class).inNamespace(namespace).list().getItems();
    }

    /**
     *
     */
    public Optional<ReleaseCRD> findById(String namespace, String releaseId) {
        log.trace("{} -> get release: {}", namespace, releaseId);
        try {
            return Optional.of(kubernetes.resources(ReleaseCRD.class)
                    .inNamespace(namespace).withName(releaseId).get());
        } catch (Exception e) {
            log.trace("{} -> release not found -> {}", namespace, e.getMessage());
        }
        return Optional.empty();
    }

    // -------------------------------

    /**
     *
     */
    public Optional<ReleaseCRD> save(ReleaseCRD release) {
        log.trace("{} -> save release: {}", release.getMetadata().getNamespace(), release.getMetadata().getName());
        try {
            return Optional.of(kubernetes.resources(ReleaseCRD.class)
                .inNamespace(release.getMetadata().getNamespace()).resource(release).create());
        } catch (Exception e) {
            log.trace("{} -> release not created -> {}", release.getMetadata().getNamespace(), e.getMessage());
        }
        return Optional.empty();
    }

    /**
     *
     */
    public Optional<ReleaseCRD> update(ReleaseCRD release) {
        log.trace("{} -> update release: {}", release.getMetadata().getNamespace(), release.getMetadata().getName());
        try {
            return Optional.of(kubernetes.resources(ReleaseCRD.class)
                    .inNamespace(release.getMetadata().getNamespace()).resource(release).update());
        } catch (Exception e) {
            log.trace("{} -> release not updated -> {}", release.getMetadata().getNamespace(), e.getMessage());
        }
        return Optional.empty();
    }

    /**
     *
     */
    public Optional<ReleaseCRD> patch(ReleaseCRD release) {
        log.trace("{} -> patch status: {} - {}", release.getMetadata().getNamespace(), release.getMetadata().getName(), release.getStatus().status());
        try {
            return Optional.of(kubernetes.resources(ReleaseCRD.class)
                    .inNamespace(release.getMetadata().getNamespace()).resource(release).patchStatus());
        } catch (Exception e) {
            log.trace("{} -> release not patched -> {}", release.getMetadata().getNamespace(), e.getMessage());
        }
        return Optional.empty();
    }

    /**
     *
     */
    public Optional<List<StatusDetails>> delete(ReleaseCRD release) {
        log.trace("{} -> delete release: {}", release.getMetadata().getNamespace(), release.getMetadata().getName());
        try {
            return Optional.of(kubernetes.resources(ReleaseCRD.class)
                    .inNamespace(release.getMetadata().getNamespace()).resource(release).delete());
        } catch (Exception e) {
            log.trace("{} -> release not deleted -> {}", release.getMetadata().getNamespace(), e.getMessage());
        }
        return Optional.empty();
    }

    // -------------------------------

    /**
     *
     */
    public List<ReleaseCRDSpec> findRevisions(String namespace, String releaseId) {
        log.trace("{} -> find release revisions: {}", namespace, releaseId);
        try {
            var optional = this.findById(namespace, releaseId);
            if (optional.isPresent()) {
                return optional.get().getRevision();
            }
        } catch (Exception e) {
            log.trace("{} -> revisions not found -> {}", namespace, e.getMessage());
        }        
        return List.of();
    }

    /**
     *
     */
    public Optional<ReleaseCRDSpec> findRevision(String namespace, String releaseId, Long revisionVersion) {
        log.trace("{} -> find release revision: {}", namespace, releaseId);
        try {
            var optional = this.findById(namespace, releaseId);
            if (optional.isPresent()) {
                return optional.get().getRevision().stream()
                    .filter(r -> r.version().equals(revisionVersion)).findAny();
            }
        } catch (Exception e) {
            log.trace("{} -> revision not found -> {}", namespace, e.getMessage());
        }        
        return Optional.empty();
    }

    

}
