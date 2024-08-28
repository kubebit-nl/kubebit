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

    void createManifest(InputStream inputStream, String projectId, String releaseId, String releaseVersion, File targetFile) throws IOException;

    List<ReleaseResourceRef> applyManifest(File manifestFile) throws IOException;

}
