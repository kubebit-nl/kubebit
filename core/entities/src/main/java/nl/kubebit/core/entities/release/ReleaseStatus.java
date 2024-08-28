package nl.kubebit.core.entities.release;

import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public enum ReleaseStatus {

    @JsonProperty("unknown")
    UNKNOWN,

    @JsonProperty("pending-install")
    PENDING_INSTALL,

    @JsonProperty("pending-upgrade")
    PENDING_UPGRADE,

    @JsonProperty("pending-rollback")
    PENDING_ROLLBACK,

    @JsonProperty("pending-patch")
    PENDING_PATCH,

    @JsonProperty("deployed")
    DEPLOYED,

    @JsonProperty("superseded")
    SUPERSEDED,

    @JsonProperty("failed")
    FAILED,

    @JsonProperty("uninstalling")
    UNINSTALLING,

    @JsonProperty("uninstalled")
    UNINSTALLED;

    /**
     *
     */
    public static boolean isRunning(ReleaseStatus status) {
        var running = EnumSet.of(PENDING_INSTALL, PENDING_UPGRADE, PENDING_ROLLBACK, UNINSTALLING);
        return running.contains(status);
    }

}
