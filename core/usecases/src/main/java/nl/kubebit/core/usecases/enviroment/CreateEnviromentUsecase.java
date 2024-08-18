package nl.kubebit.core.usecases.enviroment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import nl.kubebit.core.entities.enviroment.exception.EnviromentNotCreatedException;
import nl.kubebit.core.usecases.enviroment.dto.EnviromentResponse;

/**
 * 
 */
public interface CreateEnviromentUsecase {
    
    //
    EnviromentResponse execute(String projectId, EnviromentRequest request) throws EnviromentNotCreatedException;

    /**
     * 
     */
    public record EnviromentRequest(

        @NotBlank
        @Size(min = 2, max = 50)
        @Pattern(regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?")
        String name,

        @Size(max = 150)
        String description

    ) {
    }

}
