package nl.kubebit.core.usecases.release;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.enviroment.exception.EnviromentNotFoundException;
import nl.kubebit.core.entities.enviroment.gateway.EnviromentGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;

import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseRef;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.TemplateRef;
import nl.kubebit.core.entities.release.exception.ReleaseIsRunningException;
import nl.kubebit.core.entities.release.exception.ReleaseNotCreatedException;
import nl.kubebit.core.entities.release.exception.ReleaseNotFoundException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.exception.TemplateInvalidStatusException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;

import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
@Usecase
public class UpdateReleasesUsecaseImpl implements UpdateReleasesUsecase {
    // --------------------------------------------------------------------------------------------
    
    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final EnviromentGateway enviromentGateway;
    private final TemplateGateway templateGateway;
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
    public UpdateReleasesUsecaseImpl(
            ProjectGateway projectGateway, 
            EnviromentGateway enviromentGateway,
            TemplateGateway templateGateway, 
            ReleaseGateway releaseGateway, 
            ManifestGateway manifestGateway) {
        this.projectGateway = projectGateway;
        this.enviromentGateway = enviromentGateway;
        this.templateGateway = templateGateway;
        this.releaseGateway = releaseGateway;
        this.manifestGateway = manifestGateway;
    }

    /**
     * 
     */
    @Override
    public ReleaseResponse execute(String projectId, String enviromentName, String releaseId, ReleaseUpdateRequest request) {
        log.info("{} - {} -> fetch releases", projectId, enviromentName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var enviroment = enviromentGateway.findByName(project, enviromentName).orElseThrow(() -> new EnviromentNotFoundException(enviromentName));
        var release = releaseGateway.findById(enviroment.id(), releaseId).orElseThrow(() -> new ReleaseNotFoundException(releaseId));
        
        //
        if(ReleaseStatus.isRunning(release.status())) {
            throw new ReleaseIsRunningException(release.id());
        }
        
        //
        var template = templateGateway.findById(request.templateId()).orElseThrow(() -> new TemplateNotFoundException(request.templateId()));
        if(template.status() != TemplateStatus.AVAILABLE) {
            throw new TemplateInvalidStatusException(template.status());
        }

        // update release
        var update  = new Release(
            release.id(), 
            release.version() + 1,
            new TemplateRef(template.chart(), template.version()),
            request.values(),
            null,
            ReleaseStatus.PENDING_UPGRADE,
            null,
            null,
            getRevisions(release),
            enviroment.id());
        
        // update release
        releaseGateway.update(update).orElseThrow(() -> new ReleaseNotCreatedException());

        // install manifest
        manifestGateway.installManifest(project.id(), enviroment.name(), update, template);

        // return response
        return new ReleaseResponse(update);
    }
    
    /**
     * 
     * @param release
     * @return
     */
    public static List<ReleaseRef> getRevisions(Release release) {
        var ref = new ReleaseRef(release.version(), release.template(), release.values());
        List<ReleaseRef> result = release.revisions() == null ? new ArrayList<>() : new ArrayList<>(release.revisions());
        result.add(ref);

        // trim the list to keep only the last 5 elements
        if (result.size() > 5) {
            result = result.subList(result.size() - 5, result.size());
        }
        return result;
    }

}
