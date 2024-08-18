package nl.kubebit.core.infrastructure.enviroment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.enviroment.FetchEnviromentsUsecase;
import nl.kubebit.core.usecases.enviroment.dto.EnviromentResponse;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 */
@Tag(name = "Enviroment")
@RestController
@RequestMapping("/api/v1/project/{project_id}/enviroment")
public class FetchEnviromentsController {
    // --------------------------------------------------------------------------------------------

    //
    private final FetchEnviromentsUsecase usecase;

    /**
     * 
     * @param usecase
     */
    public FetchEnviromentsController(FetchEnviromentsUsecase usecase) {
        this.usecase = usecase;
    }

    /**
     * 
     * @return
     */
    @GetMapping
    public List<EnviromentResponse> getProject(
        @PathVariable("project_id") String projectId) {
        return usecase.execute(projectId);
    }
    
}
