package nl.kubebit.core.infrastructure.release.controller;

import nl.kubebit.core.usecases.release.dto.ReleaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import nl.kubebit.core.usecases.release.DeleteReleaseUseCase;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 */
@Tag(name = "Release")
@RestController
@RequestMapping("/api/v1/projects/{project_id}/namespaces/{namespace_name}/releases/{release_id}")
public class DeleteReleaseController {
    // --------------------------------------------------------------------------------------------

    //
    private final DeleteReleaseUseCase useCase;

    /**
     *
     */
    public DeleteReleaseController(DeleteReleaseUseCase UseCase) {
        this.useCase = UseCase;
    }

    /**
     * Delete a release
     *
     * @param projectId     the project id
     * @param namespaceName the namespace name
     * @param releaseId     the release id
     * @return the response
     */
    @DeleteMapping
    public ReleaseResponse deleteRelease(
            @PathVariable("project_id") String projectId,
            @PathVariable("namespace_name") String namespaceName,
            @PathVariable("release_id") String releaseId) {
        return useCase.execute(projectId, namespaceName, releaseId);
    }

}
