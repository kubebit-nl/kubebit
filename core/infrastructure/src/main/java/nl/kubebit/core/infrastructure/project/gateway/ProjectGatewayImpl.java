package nl.kubebit.core.infrastructure.project.gateway;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.infrastructure.project.datasource.ProjectMapper;
import nl.kubebit.core.infrastructure.project.datasource.ProjectRepository;
import nl.kubebit.core.usecases.common.annotation.Gateway;

/**
 * 
 */
@Gateway
public class ProjectGatewayImpl implements ProjectGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    //
    private final ProjectRepository repository;

    /**
     *
     */
    public ProjectGatewayImpl(ProjectRepository repository) {
        this.repository = repository;
    }

    /**
     * 
     */
    @Override
    public List<Project> findAll() {
        log.debug("find all projects");
        return repository.findAll().stream().map(ProjectMapper::toEntity).toList();
    }

    /**
     * 
     */
    @Override
    public Optional<Project> findById(String projectId) {
        log.debug("find project: {}", projectId);
        return repository.findById(projectId).map(ProjectMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public Optional<Project> save(Project project) {
        log.debug("save project: {}", project);
        return repository.save(ProjectMapper.toSchema(project)).map(ProjectMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public Optional<Project> update(Project project) {
        log.debug("update project: {}", project);
        return repository.update(ProjectMapper.toSchema(project)).map(ProjectMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public void delete(Project project) throws ProjectNotFoundException {
        log.debug("delete project: {}", project);
        repository.delete(ProjectMapper.toSchema(project));
    }
    
}
