package nl.kubebit.core.usecases.release;

import java.nio.file.Paths;
import java.util.Optional;

import nl.kubebit.core.entities.release.exception.ManifestNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import nl.kubebit.core.entities.namespace.exception.NamespaceNotFoundException;
import nl.kubebit.core.entities.namespace.gateway.NamespaceGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.exception.ReleaseNotFoundException;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;

/**
 * 
 */
@UseCase
class GetReleaseManifestUseCaseImpl implements GetReleaseManifestUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final NamespaceGateway namespaceGateway;
    private final ReleaseGateway releaseGateway;

    /**
     * Constructor
     * @param projectGateway project gateway
     * @param namespaceGateway namespace gateway
     * @param releaseGateway release gateway
     */
    public GetReleaseManifestUseCaseImpl(
            ProjectGateway projectGateway, 
            NamespaceGateway namespaceGateway,
            ReleaseGateway releaseGateway) {
        this.projectGateway = projectGateway;
        this.namespaceGateway = namespaceGateway;
        this.releaseGateway = releaseGateway;
    }

    /**
     * Fetch release manifest
     * @param projectId project id
     * @param namespaceName namespace id
     * @param releaseId release id
     * @param revisionVersion revision version
     * @return resource
     */
    @Override
    public Optional<Resource> execute(String projectId, String namespaceName, String releaseId, Long revisionVersion) {
        log.info("{} - {} -> get manifest", projectId, namespaceName);
        var project = projectGateway.findById(projectId).orElseThrow(ProjectNotFoundException::new);
        var namespace = namespaceGateway.findByName(project.id(), namespaceName).orElseThrow(NamespaceNotFoundException::new);
        var release = releaseGateway.findById(namespace.id(), releaseId).orElseThrow(ReleaseNotFoundException::new);
        
        //
        var dirPath = Paths.get("/.kubebit/manifests/", project.id(), namespace.name(), release.id());
        var version = revisionVersion == null ? release.version() : revisionVersion;
        var filePath = Paths.get(dirPath.toString(), version + ".yaml");
        var manifestFile = filePath.toFile();

        // check if manifest exists
        if(!manifestFile.exists()) {
            throw new ManifestNotFoundException(manifestFile.getName());
        }

        //
        try {
            return Optional.of(new UrlResource(manifestFile.toURI()));
        } catch (Exception e) {
            log.warn("--> {}", e.getMessage());
        }
        return Optional.empty();
    }

}
