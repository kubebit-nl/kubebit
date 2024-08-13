package nl.kubebit.core.usecases.release;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.enviroment.exception.EnviromentNotFoundException;
import nl.kubebit.core.entities.enviroment.gateway.EnviromentGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.TemplateRef;
import nl.kubebit.core.entities.release.exception.ReleaseNotCreatedException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.exception.TemplateInvalidStatusException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;

import nl.kubebit.core.usecases.release.dto.ReleaseResponse;
import nl.kubebit.core.usecases.common.annotation.Usecase;

/**
 * 
 */
@Usecase
public class CreateReleaseUsecaseImpl implements CreateReleaseUsecase {
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
    public CreateReleaseUsecaseImpl(ProjectGateway projectGateway, EnviromentGateway enviromentGateway,
            TemplateGateway templateGateway, ReleaseGateway releaseGateway, ManifestGateway manifestGateway) {
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
    public ReleaseResponse execute(String projectId, String enviromentName, ReleaseCreateRequest request) {
        log.info("{} - {} -> create release", projectId, enviromentName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var enviroment = enviromentGateway.findByName(project, enviromentName).orElseThrow(() -> new EnviromentNotFoundException(enviromentName));
        var template = templateGateway.findById(request.templateId()).orElseThrow(() -> new TemplateNotFoundException(request.templateId()));

        //
        if (template.status() != TemplateStatus.VALIDATED) {
            throw new TemplateInvalidStatusException(template.status());
        }

        //
        var release = new Release(
                request.name(),
                1L,
                new TemplateRef(template.chart(), template.version()),
                request.values(),
                null,
                ReleaseStatus.PENDING_INSTALL,
                null,
                null,
                List.of(),
                enviroment.id());

        // create release
        releaseGateway.create(release).orElseThrow(() -> new ReleaseNotCreatedException());

        // install manifest
        manifestGateway.installManifest(project.id(), enviroment.name(), release, template);

        // return response
        return new ReleaseResponse(release);
    }
}
