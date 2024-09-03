package nl.kubebit.core.usecases.release.admin;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.exception.ReleaseNotUpdatedException;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.release.dto.ReleaseResponse;
import nl.kubebit.core.usecases.release.chore.ManifestAsyncMigrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
@UseCase
class MigrateReleaseUseCaseImpl implements MigrateReleaseUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;
    private final ReleaseGateway releaseGateway;
    private final ManifestAsyncMigrator manifestMigrator;

    /**
     * Constructor
     *
     * @param projectGateway   project gateway
     * @param namespaceGateway namespace gateway
     * @param releaseGateway   release gateway
     * @param manifestMigrator manifest migrator
     */
    public MigrateReleaseUseCaseImpl(ProjectGateway projectGateway, NamespaceGateway namespaceGateway, ReleaseGateway releaseGateway, ManifestAsyncMigrator manifestMigrator) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.releaseGateway = releaseGateway;
        this.manifestMigrator = manifestMigrator;
    }

    /**
     * Create a release from a helm deployment
     *
     * @param projectId      the project id
     * @param namespaceName  the namespace name
     * @param deploymentName the helm deployment name
     */
    @Override
    public ReleaseResponse execute(String projectId, String namespaceName, String deploymentName) {
        log.info("{} - {} -> create release", projectId, namespaceName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var namespace = namespaceGateway.findByName(project, namespaceName).orElseThrow(() -> new NamespaceNotFoundException(namespaceName));

        // create release
        var release = new Release(
                deploymentName,
                1L,
                null,
                null,
                null,
                ReleaseStatus.PENDING_INSTALL,
                null,
                null,
                List.of(),
                namespace.id());

        // create release
        releaseGateway.create(release).orElseThrow(ReleaseNotUpdatedException::new);

        // install manifest async
        manifestMigrator.execute(project, namespace, release);

        // return response
        return new ReleaseResponse(release);
    }

}
