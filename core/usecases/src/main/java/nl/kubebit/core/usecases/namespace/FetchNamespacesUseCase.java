package nl.kubebit.core.usecases.namespace;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

/**
 * 
 */
public interface FetchNamespacesUseCase {

    List<NamespaceResponse> execute(

            @NotBlank
            String projectId
    );
    
}
