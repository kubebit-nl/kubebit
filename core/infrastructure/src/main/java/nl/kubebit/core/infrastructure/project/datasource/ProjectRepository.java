package nl.kubebit.core.infrastructure.project.datasource;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import io.fabric8.kubernetes.client.KubernetesClient;
import nl.kubebit.core.infrastructure.project.schema.ProjectCRD;

import static nl.kubebit.core.infrastructure.configuration.CacheConfig.CACHE_PROJECTS;
import static nl.kubebit.core.infrastructure.configuration.CacheConfig.CACHE_PROJECT;

/**
 * 
 */
@Repository
public class ProjectRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    //
    private final String CACHE_PROJECT_KEY = "#project.getMetadata().getName()";
    private final String CACHE_PROJECT_UNLESS = "#result == null";

    //
    private final KubernetesClient kubernetes;

    /**
     *
     */
    public ProjectRepository(KubernetesClient kubernetes) {
        this.kubernetes = kubernetes;
    }

    /**
     *
     */
    @Cacheable(CACHE_PROJECTS)
    public List<ProjectCRD> findAll() {
        log.trace("--> find all projects");
        return kubernetes.resources(ProjectCRD.class).list().getItems();
    }

    /**
     *
     */
    @Cacheable(value = CACHE_PROJECT, key = "#id", unless = CACHE_PROJECT_UNLESS)
    public Optional<ProjectCRD> findById(String id) {
        log.trace("--> find project: {}", id);
        try {
            return Optional.of(kubernetes.resources(ProjectCRD.class).withName(id).get());
        } catch (Exception e) {
            log.trace("project {} not found -> {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    /**
     *
     */
    @Caching(
        put = {
            @CachePut(value = CACHE_PROJECT, key = CACHE_PROJECT_KEY, unless = CACHE_PROJECT_UNLESS)
        },
        evict = { 
            @CacheEvict(value = CACHE_PROJECTS, allEntries = true)
        }
    )
    public Optional<ProjectCRD> save(ProjectCRD project) {
        log.trace("--> save project: {}", project.getMetadata().getName());
        try {
            return Optional.of(kubernetes.resources(ProjectCRD.class).resource(project).create());
        } catch (Exception e) {
            log.trace("project not created -> {}", e.getMessage());
        }
        return Optional.empty();
    }

    /**
     *
     */
    @Caching( 
        put = {
            @CachePut(value = CACHE_PROJECT, key = CACHE_PROJECT_KEY, unless = CACHE_PROJECT_UNLESS)
        },
        evict = { 
            @CacheEvict(value = CACHE_PROJECTS, allEntries = true)
        }
    )
    public Optional<ProjectCRD> update(ProjectCRD project) {
        log.trace("--> update project: {}", project.getMetadata().getName());
        try {
            return Optional.of(kubernetes.resources(ProjectCRD.class).resource(project).update());
        } catch (Exception e) {
            log.trace("project not updated -> {}", e.getMessage());
        }
        return Optional.empty();
    }

    /**
     */
    @Caching( 
        evict = { 
            @CacheEvict(value = CACHE_PROJECT, key = CACHE_PROJECT_KEY),
            @CacheEvict(value = CACHE_PROJECTS, allEntries = true)
        }
    )
    public void delete(ProjectCRD project) {
        log.trace("--> delete project: {}", project);
        try {
            kubernetes.resources(ProjectCRD.class).resource(project).delete();
        } catch (Exception e) {
            log.trace("project not deleted -> {}", e.getMessage());
        }
    }
}
