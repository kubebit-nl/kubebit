package nl.kubebit.core.infrastructure.project.datasource;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.client.KubernetesClient;
import nl.kubebit.core.infrastructure.project.schema.ProjectCRD;

/**
 * 
 */
@Repository
public class ProjectRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    //
    private final KubernetesClient kubernetes;

    /**
     * 
     * @param kubernetes
     */
    public ProjectRepository(KubernetesClient kubernetes) {
        this.kubernetes = kubernetes;
    }

    /**
     * 
     * @return
     */
    public List<ProjectCRD> findAll() {
        log.trace("find all projects");
        return kubernetes.resources(ProjectCRD.class).list().getItems();
    }

    /**
     * 
     * @param id
     * @return
     */
    public Optional<ProjectCRD> findById(String id) {
        log.trace("find project by id: {}", id);
        try {
            return Optional.of(kubernetes.resources(ProjectCRD.class).withName(id).get());
        } catch (Exception e) {
            log.trace("project {} not found -> {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * 
     * @param project
     * @return
     */
    public Optional<ProjectCRD> save(ProjectCRD project) {
        log.trace("save project: {}", project);
        try {
            return Optional.of(kubernetes.resources(ProjectCRD.class).resource(project).create());
        } catch (Exception e) {
            log.trace("project not created -> {}", e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * 
     * @param project
     * @return
     */
    public Optional<ProjectCRD> update(ProjectCRD project) {
        log.trace("update project: {}", project);
        try {
            return Optional.of(kubernetes.resources(ProjectCRD.class).resource(project).update());
        } catch (Exception e) {
            log.trace("project not updated -> {}", e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * 
     * @param project
     * @return 
     */
    public boolean delete(ProjectCRD project) {
        log.trace("delete project: {}", project);
        try {
            kubernetes.resources(ProjectCRD.class).resource(project).delete();
            return true;
        } catch (Exception e) {
            log.trace("project not deleted -> {}", e.getMessage());
        }
        return false;        
    }
}
