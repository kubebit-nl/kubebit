package nl.kubebit.core.infrastructure.release.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.release.admin.MigrateReleaseUsecase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Tag(name = "Migrate")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces/{namespace_name}/migrate/{deployment_name}")
public class MigrateController {
    // --------------------------------------------------------------------------------------------

    //
    private final MigrateReleaseUsecase migrateReleaseUsecase;

    /**
     * Constructor
     *
     * @param migrateReleaseUsecase the migrate release usecase
     */
    public MigrateController(MigrateReleaseUsecase migrateReleaseUsecase) {
        this.migrateReleaseUsecase = migrateReleaseUsecase;
    }

    /**
     * Migrate a release
     *
     * @param projectId      the project id
     * @param namespaceName  the namespace name
     * @param deploymentName the helm deployment name
     * @return the release response
     */
    @PostMapping
    public ReleaseResponse migrate(
            @PathVariable("project_id") String projectId,
            @PathVariable("namespace_name") String namespaceName,
            @PathVariable("deployment_name") String deploymentName) {
        return migrateReleaseUsecase.execute(projectId, namespaceName, deploymentName);
    }
}
