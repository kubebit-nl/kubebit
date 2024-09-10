package nl.kubebit.core.usecases.release.chore;

import nl.kubebit.core.entities.release.Manifest;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.resource.gateway.ResourceGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.EnumSet;

/**
 *
 */
public class ManifestActionDelete implements ManifestAction {
    // ------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ResourceGateway resourceGateway;

    /**
     * Constructor
     *
     * @param resourceGateway resource gateway
     */
    public ManifestActionDelete(ResourceGateway resourceGateway) {
        this.resourceGateway = resourceGateway;
    }

    /**
     * Create a new manifest
     *
     * @param manifest the manifest
     */
    @Override
    public void execute(Manifest manifest) throws IOException {

        log.trace("deleting resources");
        manifest.release().resources()
                .forEach(resource -> resourceGateway.removeResource(manifest.namespace().id(), resource));

        log.trace("deleting revisions");
        deleteRevisions(manifest);

    }

    /**
     * Close the manifest action
     */
    @Override
    public void close() {
        // nothing to do
    }

    /**
     * Get al list of statuses that are allowed for this action
     *
     * @return the set of statuses
     */
    public static EnumSet<ReleaseStatus> statusAllowed() {
        return EnumSet.of(
                ReleaseStatus.UNINSTALLING);
    }

    // ------------------------------------------------------------------------
    // private methods

    /**
     * Delete revisions of the release
     *
     * @param manifest the manifest
     */
    private void deleteRevisions(Manifest manifest) {
        try {
            Files.walk(manifest.getFilePath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (!file.delete()) {
                            log.warn("failed to delete manifest file: {}", file);
                        }
                    });
        } catch (Exception e) {
            log.warn("failed to clear revisions: {}", e.getMessage());
        }
    }

}
