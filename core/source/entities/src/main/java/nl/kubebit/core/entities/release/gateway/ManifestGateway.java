package nl.kubebit.core.entities.release.gateway;

import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.template.Template;

/**
 * 
 */
public interface ManifestGateway {
    // --------------------------------------------------------------------------------------------

    //
    void installManifest(String projectId, String envirmentName, Release release, Template template);

    //
    void patchManifest(String projectId, String envirmentName, Release release);
    
}
