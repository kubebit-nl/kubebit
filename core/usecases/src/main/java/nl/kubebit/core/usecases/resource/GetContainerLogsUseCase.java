package nl.kubebit.core.usecases.resource;

import jakarta.validation.constraints.NotBlank;

/**
 * 
 */
public interface GetContainerLogsUseCase {

    String execute(

            @NotBlank
            String projectId,

            @NotBlank
            String namespaceName,

            @NotBlank
            String podName,

            @NotBlank
            String containerName);
    
}
