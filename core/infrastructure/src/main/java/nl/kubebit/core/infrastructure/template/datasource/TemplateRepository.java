package nl.kubebit.core.infrastructure.template.datasource;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.client.KubernetesClient;
import nl.kubebit.core.infrastructure.template.schema.TemplateCRD;

/**
 * 
 */
@Repository
public class TemplateRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final KubernetesClient kubernetes;

    /**
     * 
     * @param kubernetes
     */
    public TemplateRepository(KubernetesClient kubernetes) {
        this.kubernetes = kubernetes;
    }

    /**
     * 
     * @return
     */
    public List<TemplateCRD> findAll() {
        log.trace("find all templates");
        return kubernetes.resources(TemplateCRD.class).list().getItems();
    }

    /**
     * 
     * @param templateId
     * @return
     */
    public Optional<TemplateCRD> findById(String templateId) {
        log.trace("find template by id: {}", templateId);
        return Optional.ofNullable(kubernetes.resources(TemplateCRD.class).withName(templateId).get());
    }

    /**
     * 
     * @param namespace
     * @param release
     * @return
     */
    public Optional<TemplateCRD> save(TemplateCRD release) {
        log.trace("save template: {}", release.getMetadata().getName());
        try {
            return Optional.of(kubernetes.resources(TemplateCRD.class).resource(release).create());
        } catch (Exception e) {
            log.trace("template not created -> {}", e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * 
     * @param release
     * @return
     */
    public Optional<TemplateCRD> update(TemplateCRD release) {
        log.trace("update template: {}", release.getMetadata().getName());
        try {
            return Optional.of(kubernetes.resources(TemplateCRD.class).resource(release).update());
        } catch (Exception e) {
            log.error("template not updated -> {}", e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * 
     * @param release
     * @return
     */
    public Optional<TemplateCRD> updateStatus(TemplateCRD release) {
        log.trace("update status template: {}", release.getMetadata().getName());
        try {
            return Optional.of(kubernetes.resources(TemplateCRD.class).resource(release).updateStatus());
        } catch (Exception e) {
            log.error("template not updated -> {}", e.getMessage());
        }
        return Optional.empty();
    }
}
