package nl.kubebit.core.infrastructure.namespace.gateway;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.infrastructure.namespace.datasource.NamespaceMapper;
import nl.kubebit.core.infrastructure.namespace.datasource.NamespaceRepository;
import nl.kubebit.core.usecases.common.annotation.Gateway;

/**
 * 
 */
@Gateway
public class NamespaceGatewayImpl implements NamespaceGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    //
    private final NamespaceRepository repository;

    /**
     *
     */
    public NamespaceGatewayImpl(NamespaceRepository repository) {
        this.repository = repository;
    }

    /**
     * 
     */
    @Override
    public List<Namespace> findAll(Project project) {
        log.trace("{} -> find all namespaces", project.id());
        return repository.findAllByProject(project.id()).stream().map(NamespaceMapper::toEntity).toList();
    }

    /**
     * 
     */
    @Override
    public Optional<Namespace> findById(Project project, String namespaceId) {
        log.trace("{} -> find namespace by id: {}", project.id(), namespaceId);
        return repository.findByProjectAndId(project.id(), namespaceId).map(NamespaceMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public Optional<Namespace> findByName(Project project, String namespaceName) {
        log.trace("{} -> find namespace by name: {}", project.id(), namespaceName);
        return findById(project, Namespace.formatId(project.id(), namespaceName));
    }

    /**
     * 
     */
    @Override
    public Optional<Namespace> save(Namespace namespace) {
        log.trace("{} -> save namespace: {}", namespace.projectId(), namespace.name());
        return repository.save(namespace.projectId(), NamespaceMapper.toSchema(namespace)).map(NamespaceMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public Optional<Namespace> update(Namespace namespace) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    /**
     * 
     */
    @Override
    public void delete(Namespace namespace) throws NamespaceNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    /**
     * 
     */
    @Override
    public Boolean unique(String namespaceId) {
        return repository.findAll().stream().filter(n -> n.getMetadata().getName().equals(namespaceId)).findAny().isEmpty();
    }

    

    
    
}
