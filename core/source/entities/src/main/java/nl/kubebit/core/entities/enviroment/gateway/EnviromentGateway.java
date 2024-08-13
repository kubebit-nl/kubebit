package nl.kubebit.core.entities.enviroment.gateway;

import java.util.List;
import java.util.Optional;

import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.enviroment.Enviroment;
import nl.kubebit.core.entities.enviroment.exception.EnviromentNotFoundException;

/**
 * 
 */
public interface EnviromentGateway {

    List<Enviroment> findAll(Project project);

    Optional<Enviroment> findById(Project project, String enviromentId);

    Optional<Enviroment> findByName(Project project, String enviromentName);

    Optional<Enviroment> save(Enviroment enviroment);

    Optional<Enviroment> update(Enviroment enviroment);

    void delete(Enviroment enviroment) throws EnviromentNotFoundException;

    Boolean unique(String id);

}
