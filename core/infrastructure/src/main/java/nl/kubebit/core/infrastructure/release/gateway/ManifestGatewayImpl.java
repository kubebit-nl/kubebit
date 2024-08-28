package nl.kubebit.core.infrastructure.release.gateway;

import nl.kubebit.core.entities.release.ReleaseResourceRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.infrastructure.release.datasource.ManifestRepository;
import nl.kubebit.core.usecases.common.annotation.Gateway;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 
 */
@Gateway
public class ManifestGatewayImpl implements ManifestGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ManifestRepository manifestRepository;

    /**
     * Constructor
     * @param manifestRepository manifest repository
     */
    public ManifestGatewayImpl(ManifestRepository manifestRepository) {
        this.manifestRepository = manifestRepository;
    }

    /**
     * Apply manifest to kubernetes
     * @param manifestFile manifest file
     * @return list of resources
     * @throws IOException error reading manifest file
     */
    @Override
    public List<ReleaseResourceRef> applyManifest(File manifestFile) throws IOException {
        log.debug("apply manifest: {}", manifestFile.getAbsolutePath());
        return manifestRepository.applyManifest(manifestFile);
    }

    /**
     * Create manifest from input stream
     * @param inputStream input stream form helm template
     * @param projectId project id
     * @param releaseId release id
     * @param releaseVersion release version
     * @param targetFile manifest file destination
     * @throws IOException error writing manifest file
     */
    @Override
    public void createManifest(InputStream inputStream, String projectId, String releaseId, String releaseVersion, File targetFile) throws IOException {
        log.debug("create manifest: {}", targetFile.getAbsolutePath());
        manifestRepository.createManifest(inputStream, projectId, releaseId, releaseVersion, targetFile);
    }

}
