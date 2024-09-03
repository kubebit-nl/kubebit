package nl.kubebit.core.usecases.release.chore;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.exception.ManifestNotFoundException;
import nl.kubebit.core.entities.release.exception.ReleaseNotUpdatedException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.nio.file.Paths;

import static nl.kubebit.core.usecases.common.vars.GlobalVars.MANIFESTS_LOCATION;
import static nl.kubebit.core.usecases.common.vars.GlobalVars.YAML_EXT;

/**
 *
 */
@Component
@Validated
public class ManifestAsyncPatcher {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ReleaseGateway releaseGateway;
    private final ManifestGateway manifestGateway;

    /**
     * Constructor
     * @param releaseGateway release gateway
     * @param templateGateway template gateway
     * @param manifestGateway manifest gateway
     */
    public ManifestAsyncPatcher(ReleaseGateway releaseGateway, TemplateGateway templateGateway, ManifestGateway manifestGateway) {
        this.releaseGateway = releaseGateway;
        this.manifestGateway = manifestGateway;
    }

    /**
     * Execute the patching of the release
     * @param project the project
     * @param namespace the namespace
     * @param release the release
     */
    @Async
    public void execute(Project project, Namespace namespace, Release release) {
        log.info("{} -> patching release: {}", release.namespaceId(), release.version());
        try {
            // set pending status
            releaseGateway.patch(release).orElseThrow(ReleaseNotUpdatedException::new);

            //
            var manifestPath = Paths.get(MANIFESTS_LOCATION, project.id(), namespace.name(), release.id());
            var manifestFile = Paths.get(manifestPath.toString(), release.version() + YAML_EXT).toFile();
            log.trace("manifest file: {}", manifestFile);

            // check if manifest exists
            if(!manifestFile.exists()) {
                throw new ManifestNotFoundException(manifestFile.getName());
            }

            log.trace("patching manifest...");
            manifestGateway.applyManifest(manifestFile);

            // set deployed status
            releaseGateway.patch(release.setDeployedAndResources(release.resources())).orElseThrow(ReleaseNotUpdatedException::new);

        } catch (Exception e) {
            log.warn("{} -> error patching release: {}", namespace.id(), release.version(), e);
            if(releaseGateway.patch(release.setStatus(ReleaseStatus.FAILED, e.getMessage())).isEmpty()) {
                log.error("--> failed to update release status");
            }
        }
        log.trace("thread finished");
    }
}
