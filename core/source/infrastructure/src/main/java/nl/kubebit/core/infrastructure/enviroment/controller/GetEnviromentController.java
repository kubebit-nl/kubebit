package nl.kubebit.core.infrastructure.enviroment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.enviroment.GetEnviromentUsecase;
import nl.kubebit.core.usecases.enviroment.dto.EnviromentResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Enviroment")
@RestController
@RequestMapping("/api/v1/project/{project_id}/enviroment")
public class GetEnviromentController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetEnviromentUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public GetEnviromentController(GetEnviromentUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @GetMapping("/{enviroment_name}")
    public EnviromentResponse getProject(
        @PathVariable("project_id") String projectId,
        @PathVariable("enviroment_name") String enviromentName) {
        return usecase.execute(projectId, enviromentName);
    }
    
}
