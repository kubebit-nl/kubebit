package nl.kubebit.core.usecases.release;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.enviroment.exception.EnviromentNotFoundException;
import nl.kubebit.core.entities.enviroment.gateway.EnviromentGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.exception.ReleaseIsRunningException;
import nl.kubebit.core.entities.release.exception.ReleaseNotFoundException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

@Usecase
public class PatchReleaseUsecaseImpl implements PatchReleaseUsecase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final EnviromentGateway enviromentGateway;
    private final ReleaseGateway releaseGateway;
    private final ManifestGateway manifestGateway;

    /**
     * 
     * @param projectGateway
     * @param enviromentGateway
     * @param templateGateway
     * @param releaseGateway
     * @param manifestGateway
     */
    public PatchReleaseUsecaseImpl(
            ProjectGateway projectGateway, 
            EnviromentGateway enviromentGateway,
            ReleaseGateway releaseGateway, 
            ManifestGateway manifestGateway) {
        this.projectGateway = projectGateway;
        this.enviromentGateway = enviromentGateway;
        this.releaseGateway = releaseGateway;
        this.manifestGateway = manifestGateway;
    }

    /**
     * 
     */
    @Override
    public ReleaseResponse execute(String projectId, String enviromentName, String releaseId) {
        log.info("{} - {} -> patch release", projectId, enviromentName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var enviroment = enviromentGateway.findByName(project, enviromentName).orElseThrow(() -> new EnviromentNotFoundException(enviromentName));
        var release = releaseGateway.findById(enviroment.id(), releaseId).orElseThrow(() -> new ReleaseNotFoundException(releaseId));
        
        //
        if(ReleaseStatus.isRunning(release.status())) {
            throw new ReleaseIsRunningException(release.id());
        }

        // update release
        var update  = new Release(
            release.id(), 
            release.version(),
            release.template(),
            release.values(),
            null,
            ReleaseStatus.PENDING_PATCH,
            null,
            null,
            release.revisions(),
            enviroment.id());

        // install manifest
        manifestGateway.patchManifest(project.id(), enviroment.name(), update);

        // return response
        return new ReleaseResponse(update);
    }
    
}
