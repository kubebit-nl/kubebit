package nl.kubebit.core.usecases.namespace;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import nl.kubebit.core.entities.namespace.exception.NamespaceNotCreatedException;
import nl.kubebit.core.usecases.namespace.dto.NamespaceResponse;

/**
 * 
 */
public interface CreateNamespaceUsecase {
    
    //
    NamespaceResponse execute(String projectId, NamespaceRequest request) throws NamespaceNotCreatedException;

    /**
     * 
     */
    record NamespaceRequest(

        @NotBlank
        @Size(min = 2, max = 50)
        @Pattern(regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?")
        String name,

        @Size(max = 150)
        String description,

        @JsonProperty("is_default")
        boolean isDefault

    ) {
    }

}
