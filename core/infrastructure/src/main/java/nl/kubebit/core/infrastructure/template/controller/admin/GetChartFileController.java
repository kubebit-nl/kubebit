package nl.kubebit.core.infrastructure.template.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import nl.kubebit.core.usecases.template.admin.GetChartFileUseCase;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@Tag(name = "Template")
@RestController
@RequestMapping("/api/admin/v1/templates/{template_id}/file/{file_name}")
public class GetChartFileController {
    // --------------------------------------------------------------------------------------------

    //
    private final GetChartFileUseCase useCase;

    /**
     *
     */
    public GetChartFileController(GetChartFileUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     *
     */
    @Operation(summary = "Get content of a chart file")
    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String getTemplate(
            @PathVariable("template_id") String templateId,
            @PathVariable("file_name") String fileName) {
        return useCase.execute(templateId, fileName);
    }

}
