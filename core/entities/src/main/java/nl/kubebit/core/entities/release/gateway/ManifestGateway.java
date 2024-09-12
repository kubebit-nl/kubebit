package nl.kubebit.core.entities.release.gateway;

import nl.kubebit.core.entities.release.ReleaseResourceRef;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Manifest gateway
 */
public interface ManifestGateway {
    // --------------------------------------------------------------------------------------------

    /**
     * Create a manifest file
     *
     * @param inputStream    the input stream from helm template process
     * @param projectId      the project id
     * @param namespaceId    the namespace id
     * @param releaseVersion the release version
     * @param targetFile     the target file
     * @throws IOException if an error occurs
     */
    void createManifest(InputStream inputStream, String projectId, String namespaceId, Long releaseVersion, File targetFile) throws IOException;

    /**
     * Get the resources from the manifest file
     *
     * @param manifestFile the manifest file
     * @return the list of release resource references
     * @throws IOException      if an error occurs
     * @throws RuntimeException if resources are not found
     */
    List<ReleaseResourceRef> getResources(File manifestFile) throws IOException, RuntimeException;

    /**
     * Apply the manifest file
     *
     * @param manifestFile the manifest file
     * @throws IOException if an error occurs
     */
    void applyManifest(File manifestFile) throws IOException;

}
