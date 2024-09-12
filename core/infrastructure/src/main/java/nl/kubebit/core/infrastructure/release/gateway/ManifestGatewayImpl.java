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
 * Manifest gateway implementation
 */
@Gateway
public class ManifestGatewayImpl implements ManifestGateway {
    // --------------------------------------------------------------------------------------------

    // logger
    private final Logger log = LoggerFactory.getLogger(getClass());

    // manifest repository
    private final ManifestRepository manifestRepository;

    /**
     * Constructor
     *
     * @param manifestRepository manifest repository
     */
    public ManifestGatewayImpl(ManifestRepository manifestRepository) {
        this.manifestRepository = manifestRepository;
    }

    /**
     * Create manifest from input stream
     *
     * @param inputStream    input stream form helm template
     * @param projectId      project id
     * @param namespaceId    namespace id
     * @param releaseVersion release version
     * @param targetFile     manifest file destination
     * @throws IOException error writing manifest file
     */
    @Override
    public void createManifest(InputStream inputStream, String projectId, String namespaceId, Long releaseVersion, File targetFile) throws IOException {
        log.debug("create manifest: {}", targetFile.getAbsolutePath());
        manifestRepository.createManifest(inputStream, projectId, namespaceId, releaseVersion, targetFile);
    }

    /**
     * Get the resources from the manifest file
     *
     * @param manifestFile the manifest file
     * @return the list of release resource references
     */
    @Override
    public List<ReleaseResourceRef> getResources(File manifestFile) throws IOException {
        log.debug("get resources: {}", manifestFile.getName());
        return manifestRepository.getResources(manifestFile);
    }

    /**
     * Apply manifest to kubernetes
     *
     * @param manifestFile manifest file
     * @throws IOException error reading manifest file
     */
    @Override
    public void applyManifest(File manifestFile) throws IOException {
        log.debug("apply manifest: {}", manifestFile.getName());
        manifestRepository.applyManifest(manifestFile);
    }
}
