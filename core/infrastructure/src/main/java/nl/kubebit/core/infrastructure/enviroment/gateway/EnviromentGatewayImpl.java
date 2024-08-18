package nl.kubebit.core.infrastructure.enviroment.gateway;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.enviroment.Enviroment;
import nl.kubebit.core.entities.enviroment.exception.EnviromentNotFoundException;
import nl.kubebit.core.entities.enviroment.gateway.EnviromentGateway;
import nl.kubebit.core.infrastructure.enviroment.datasource.EnviromentMapper;
import nl.kubebit.core.infrastructure.enviroment.datasource.EnviromentRepository;
import nl.kubebit.core.usecases.common.annotation.Gateway;

/**
 * 
 */
@Gateway
public class EnviromentGatewayImpl implements EnviromentGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    //
    private final EnviromentRepository repository;

    /**
     * 
     * @param repository
     */
    public EnviromentGatewayImpl(EnviromentRepository repository) {
        this.repository = repository;
    }

    /**
     * 
     */
    @Override
    public List<Enviroment> findAll(Project project) {
        log.trace("{} -> find all enviroments", project.id());
        return repository.findAllByProject(project.id()).stream().map(EnviromentMapper::toEntity).toList();
    }

    /**
     * 
     */
    @Override
    public Optional<Enviroment> findById(Project project, String enviromentId) {
        log.trace("{} -> find enviroment by id: {}", project.id(), enviromentId);
        return repository.findByProjectAndId(project.id(), enviromentId).map(EnviromentMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public Optional<Enviroment> findByName(Project project, String enviromentName) {
        log.trace("{} -> find enviroment by name: {}", project.id(), enviromentName);
        return findById(project, Enviroment.formatId(project.id(), enviromentName));
    }

    /**
     * 
     */
    @Override
    public Optional<Enviroment> save(Enviroment enviroment) {
        log.trace("{} -> save enviroment: {}", enviroment.projectId(), enviroment.name());
        return repository.save(enviroment.projectId(), EnviromentMapper.toSchema(enviroment)).map(EnviromentMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public Optional<Enviroment> update(Enviroment enviroment) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    /**
     * 
     */
    @Override
    public void delete(Enviroment enviroment) throws EnviromentNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /**
     * 
     */
    @Override
    public Boolean unique(String enviromentId) {
        return repository.findAll().stream().filter(n -> n.getMetadata().getName().equals(enviromentId)).findAny().isEmpty();
    }

    

    
    
}
