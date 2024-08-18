package nl.kubebit.core.infrastructure.enviroment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import nl.kubebit.core.usecases.enviroment.CreateEnviromentUsecase;
import nl.kubebit.core.usecases.enviroment.CreateEnviromentUsecase.EnviromentRequest;
import nl.kubebit.core.usecases.enviroment.dto.EnviromentResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@Tag(name = "Enviroment")
@RestController
@RequestMapping("/api/v1/project/{project_id}/enviroment")
public class CreateEnviromentController {
    // --------------------------------------------------------------------------------------------

    //
    private final CreateEnviromentUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public CreateEnviromentController(CreateEnviromentUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @PostMapping
    public EnviromentResponse getProject(
        @PathVariable("project_id") String projectId,
        @RequestBody @Valid EnviromentRequest request) {
        return usecase.execute(projectId, request);
    }
    
}
