package nl.kubebit.core.usecases.release;

import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import nl.kubebit.core.entities.enviroment.exception.EnviromentNotFoundException;
import nl.kubebit.core.entities.enviroment.gateway.EnviromentGateway;
import nl.kubebit.core.entities.project.exception.ProjectNotFoundException;
import nl.kubebit.core.entities.project.gateway.ProjectGateway;
import nl.kubebit.core.entities.release.exception.ReleaseNotFoundException;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;

/**
 * 
 */
@Usecase
public class GetReleaseManifestUsecaseImpl implements GetReleaseManifestUsecase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ProjectGateway projectGateway;
    private final EnviromentGateway enviromentGateway;
    private final ReleaseGateway releaseGateway;

    /**
     * 
     * @param projectGateway
     * @param enviromentGateway
     * @param templateGateway
     * @param releaseGateway
     * @param manifestGateway
     */
    public GetReleaseManifestUsecaseImpl(
            ProjectGateway projectGateway, 
            EnviromentGateway enviromentGateway,
            ReleaseGateway releaseGateway) {
        this.projectGateway = projectGateway;
        this.enviromentGateway = enviromentGateway;
        this.releaseGateway = releaseGateway;
    }

    /**
     * @return 
     * 
     */
    @Override
    public Optional<Resource> execute(String projectId, String enviromentName, String releaseId, Long revisionVersion) {
        log.info("{} - {} -> fetch releases", projectId, enviromentName);
        var project = projectGateway.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        var enviroment = enviromentGateway.findByName(project, enviromentName).orElseThrow(() -> new EnviromentNotFoundException(enviromentName));
        var release = releaseGateway.findById(enviroment.id(), releaseId).orElseThrow(() -> new ReleaseNotFoundException(releaseId));  
        
        //
        var dirPath = Paths.get("/.kubebit/manifests/", project.id(), enviroment.name(), release.id());
        var version = revisionVersion == null ? release.version() : revisionVersion;
        var filePath = Paths.get(dirPath.toString(), version + ".yaml");

        //
        try {
            return Optional.of(new UrlResource(filePath.toFile().toURI()));
        } catch (Exception e) {
            log.warn("--> {}", e.getMessage());
        }
        return Optional.empty();
    }

}
