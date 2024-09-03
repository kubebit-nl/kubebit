package nl.kubebit.core.usecases.namespace;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

/**
 * 
 */
public interface GetNamespaceUseCase {

    NamespaceResponse execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceId

    ) throws NamespaceNotFoundException;
    
}
