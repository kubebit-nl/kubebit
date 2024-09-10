package nl.kubebit.core.entities.namespace.gateway;

import java.util.List;
import java.util.Optional;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.project.Project;

/**
 * 
 */
public interface NamespaceGateway {

    List<Namespace> findAll(Project project);

    Optional<Namespace> findById(Project project, String namespaceId);

    Optional<Namespace> findByName(String projectId, String namespaceName);

    Optional<Namespace> save(Namespace namespace);

    Optional<Namespace> update(Namespace namespace);

    void delete(Namespace namespace) throws NamespaceNotFoundException;

    Boolean unique(String id);

}
