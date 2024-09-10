package nl.kubebit.core.usecases.release.chore;

import nl.kubebit.core.entities.release.Manifest;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

/**
 *
 */
public class ManifestActionApply implements ManifestAction {
    // ------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ManifestGateway manifestGateway;

    /**
     * Constructor
     *
     * @param manifestGateway manifest gateway
     */
    public ManifestActionApply(ManifestGateway manifestGateway) {
        this.manifestGateway = manifestGateway;
    }

    /**
     * Create a new manifest
     *
     * @param manifest the manifest
     */
    @Override
    public void execute(Manifest manifest) throws Exception {

        log.info("apply manifest {}", manifest.release().id());
        manifestGateway.applyManifest(manifest.getFile());

    }

    /**
     * Close the manifest action
     */
    @Override
    public void close() {
        // nothing to do
    }

    // ------------------------------------------------------------------------
    // private methods

    /**
     * Get al list of statuses that are allowed for this action
     *
     * @return the set of statuses
     */
    public static EnumSet<ReleaseStatus> statusAllowed() {
        return EnumSet.of(
                ReleaseStatus.PENDING_INSTALL,
                ReleaseStatus.PENDING_ROLLBACK,
                ReleaseStatus.PENDING_UPGRADE,
                ReleaseStatus.PENDING_PATCH);
    }
}
