package nl.kubebit.core.entities.release.gateway;

import nl.kubebit.core.entities.release.ReleaseResourceRef;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 */
public interface ManifestGateway {
    // --------------------------------------------------------------------------------------------

    /**
     * Create a manifest file
     *
     * @param inputStream    the input stream from helm template process
     * @param projectId      the project id
     * @param releaseId      the release id
     * @param releaseVersion the release version
     * @param targetFile     the target file
     * @throws IOException if an error occurs
     */
    void createManifest(InputStream inputStream, String projectId, String releaseId, String releaseVersion, File targetFile) throws IOException;

    /**
     * Apply the manifest file
     *
     * @param manifestFile the manifest file
     * @return the list of release resource references
     * @throws IOException if an error occurs
     */
    List<ReleaseResourceRef> applyManifest(File manifestFile) throws IOException;

    /**
     * Get the resources from the manifest file
     *
     * @param manifestFile the manifest file
     * @return the list of release resource references
     */
    List<ReleaseResourceRef> getResources(File manifestFile) throws IOException;

}
