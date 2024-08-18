package nl.kubebit.core.entities.project.gateway;

import java.util.List;
import java.util.Optional;

import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;

/**
 * 
 */
public interface ProjectGateway {

    List<Project> findAll();

    Optional<Project> findById(String projectId);

    Optional<Project> save(Project project);

    Optional<Project> update(Project project);

    void delete(Project project) throws ProjectNotFoundException;

}
