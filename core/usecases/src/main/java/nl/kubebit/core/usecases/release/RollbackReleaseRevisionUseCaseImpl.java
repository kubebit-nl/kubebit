package nl.kubebit.core.usecases.release;

import nl.kubebit.core.usecases.release.chore.ManifestAsyncChore;
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
import nl.kubebit.core.entities.release.exception.RevisionNotFoundException;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.exception.TemplateInvalidStatusException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;

/**
 * 
 */
@UseCase
class RollbackReleaseRevisionUseCaseImpl implements RollbackReleaseRevisionUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;
    private final TemplateGateway templateGateway;
    private final ReleaseGateway releaseGateway;
    private final ManifestAsyncChore manifestInstaller;

    /**
     *
     */
    public RollbackReleaseRevisionUseCaseImpl(
            ProjectGateway projectGateway,
            NamespaceGateway namespaceGateway,
            TemplateGateway templateGateway,
            ReleaseGateway releaseGateway,
            ManifestAsyncChore manifestInstaller) {
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
    public ReleaseResponse execute(String projectId, String namespaceName, String releaseId, Long revisionVersion) {
        log.info("{} - {} -> rollback release: {}", projectId, namespaceName, revisionVersion);
        var project = projectGateway.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        var namespace = namespaceGateway.findByName(project.id(), namespaceName).orElseThrow(NamespaceNotFoundException::new);
        var release = releaseGateway.findById(namespace.id(), releaseId).orElseThrow(ReleaseNotFoundException::new);
        
        // check if release is running
        if(ReleaseStatus.isRunning(release.status())) {
            throw new ReleaseIsRunningException(release.id());
        }
        
        // find revision
        var revision = releaseGateway.findRevisionById(namespace.id(), release, revisionVersion)
            .orElseThrow(RevisionNotFoundException::new);

        // find template
        var templateId = revision.template().id();
        var template = templateGateway.findById(templateId).orElseThrow(TemplateNotFoundException::new);
        if(template.status() != TemplateStatus.AVAILABLE) {
            throw new TemplateInvalidStatusException(template.status());
        }
        
        // rollback release
        var entity = new Release(
            release.id(), 
            release.version() + 1,
            new TemplateRef(template.chart(), template.version()),
            revision.values(),
            template.icon(),
            ReleaseStatus.PENDING_ROLLBACK,
            "",
            release.resources(),
            release.newRevisions(),
            namespace.id());
        
        // rollback release
        releaseGateway.update(entity).orElseThrow(ReleaseNotCreatedException::new);

        // install manifest async
        manifestInstaller.execute(project, namespace, template, entity);

        // return response
        return new ReleaseResponse(entity);
    }
    
}
