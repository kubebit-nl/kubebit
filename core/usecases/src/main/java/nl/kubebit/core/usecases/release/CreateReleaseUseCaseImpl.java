package nl.kubebit.core.usecases.release;

import java.util.List;
import java.util.Map;

import nl.kubebit.core.entities.release.exception.ReleaseAlreadyExistsException;
import nl.kubebit.core.usecases.release.chore.ManifestAsyncChore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.project.exception.ProjectNotCreatedException;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;

import nl.kubebit.core.entities.template.exception.TemplateInvalidStatusException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;

import nl.kubebit.core.entities.release.exception.ReleaseNotCreatedException;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.TemplateRef;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;

import nl.kubebit.core.usecases.common.util.JsonSchemaSanitizer;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;
import nl.kubebit.core.usecases.common.annotation.UseCase;

/**
 *
 */
@UseCase
class CreateReleaseUseCaseImpl implements CreateReleaseUseCase {
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
     * Constructor
     *
     * @param projectGateway    project gateway
     * @param namespaceGateway  namespace gateway
     * @param templateGateway   template gateway
     * @param releaseGateway    release gateway
     * @param manifestInstaller manifest installer
     */
    public CreateReleaseUseCaseImpl(ProjectGateway projectGateway, NamespaceGateway namespaceGateway, TemplateGateway templateGateway, ReleaseGateway releaseGateway, ManifestAsyncChore manifestInstaller) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.templateGateway = templateGateway;
        this.releaseGateway = releaseGateway;
        this.manifestInstaller = manifestInstaller;
    }

    /**
     * Create a release
     *
     * @param projectId     the project id
     * @param namespaceName the namespace id
     * @param request       the request
     * @return the response
     * @throws ProjectNotCreatedException if the project is not created
     */
    @Override
    public ReleaseResponse execute(String projectId, String namespaceName, ReleaseCreateRequest request) throws ProjectNotCreatedException {
        log.info("{} - {} -> create release", projectId, namespaceName);

        //
        var project = projectGateway.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        var namespace = namespaceGateway.findByName(project.id(), namespaceName).orElseThrow(NamespaceNotFoundException::new);

        //
        if (!releaseGateway.unique(namespace.id(), request.name())) {
            throw new ReleaseAlreadyExistsException();
        }

        var template = templateGateway.findById(request.templateId()).orElseThrow(TemplateNotFoundException::new);
        if (template.status() != TemplateStatus.AVAILABLE) {
            throw new TemplateInvalidStatusException(template.status());
        }

        // sanitize values based on the template schema
        Map<String, Object> sanitizeValues = JsonSchemaSanitizer.execute(template.schema(), request.values());

        // create release
        var release = new Release(
                request.name(),
                1L,
                new TemplateRef(template),
                sanitizeValues,
                template.icon(),
                ReleaseStatus.PENDING_INSTALL,
                null,
                null,
                List.of(),
                namespace.id());

        // create release
        releaseGateway.create(release).orElseThrow(ReleaseNotCreatedException::new);

        // install manifest async
        manifestInstaller.execute(project, namespace, template, release);

        // return response
        return new ReleaseResponse(release);
    }

}
