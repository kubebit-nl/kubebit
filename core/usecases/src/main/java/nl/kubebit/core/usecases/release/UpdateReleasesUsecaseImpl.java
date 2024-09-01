package nl.kubebit.core.usecases.release;

import java.util.Map;

import nl.kubebit.core.usecases.common.util.JsonSchemaSanitizer;
import nl.kubebit.core.usecases.release.util.ManifestAsyncInstaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;

import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.TemplateRef;
import nl.kubebit.core.entities.release.exception.ReleaseIsRunningException;
import nl.kubebit.core.entities.release.exception.ReleaseNotCreatedException;
import nl.kubebit.core.entities.release.exception.ReleaseNotFoundException;
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
class UpdateReleasesUsecaseImpl implements UpdateReleasesUsecase {
    // --------------------------------------------------------------------------------------------
    
    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;
    private final TemplateGateway templateGateway;
    private final ReleaseGateway releaseGateway;
    private final ManifestAsyncInstaller manifestInstaller;

    /**
     *
     */
    public UpdateReleasesUsecaseImpl(ProjectGateway projectGateway, NamespaceGateway namespaceGateway, TemplateGateway templateGateway, ReleaseGateway releaseGateway, ManifestAsyncInstaller manifestInstaller) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.templateGateway = templateGateway;
        this.releaseGateway = releaseGateway;
        this.manifestInstaller = manifestInstaller;
    }

    /**
     * 
     */
    @Override
    public ReleaseResponse execute(String projectId, String namespaceName, String releaseId, ReleaseUpdateRequest request) {
        log.info("{} - {} -> update release", projectId, namespaceName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var namespace = namespaceGateway.findByName(project, namespaceName).orElseThrow(() -> new NamespaceNotFoundException(namespaceName));
        var release = releaseGateway.findById(namespace.id(), releaseId).orElseThrow(() -> new ReleaseNotFoundException(releaseId));
        
        // check if release is running
        if(ReleaseStatus.isRunning(release.status())) {
            throw new ReleaseIsRunningException(release.id());
        }
        
        // check if template is available
        var template = templateGateway.findById(request.templateId()).orElseThrow(() -> new TemplateNotFoundException(request.templateId()));
        if(template.status() != TemplateStatus.AVAILABLE) {
            throw new TemplateInvalidStatusException(template.status());
        }

        // sanitize values based on the template schema
        Map<String, Object> sanitizeValues = JsonSchemaSanitizer.execute(template.formSchema(), request.values());

        // update release
        var entity = new Release(
            release.id(), 
            release.version() + 1,
            new TemplateRef(template.chart(), template.version()),
            sanitizeValues,
            null,
            ReleaseStatus.PENDING_UPGRADE,
            null,
            null,
            release.newRevisions(),
            namespace.id());

        // update release
        releaseGateway.update(entity).orElseThrow(ReleaseNotCreatedException::new);

        // install manifest async
        manifestInstaller.execute(project, namespace, template, entity);

        // return response
        return new ReleaseResponse(entity);
    }

}
