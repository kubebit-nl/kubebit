package nl.kubebit.core.usecases.release.chore;

import nl.kubebit.core.entities.release.Manifest;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.util.HelmBuilder;
import nl.kubebit.core.usecases.common.util.HelmValuesMerger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.stream.Collectors;

import static nl.kubebit.core.entities.common.vars.GlobalVars.YAML_EXT;

/**
 *
 */
public class ManifestActionCreate implements ManifestAction {
    // ------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway templateGateway;
    private final ManifestGateway manifestGateway;

    /**
     * Constructor
     *
     * @param templateGateway template gateway
     */
    public ManifestActionCreate(TemplateGateway templateGateway, ManifestGateway manifestGateway) {
        this.templateGateway = templateGateway;
        this.manifestGateway = manifestGateway;
    }

    /**
     * Create a new manifest
     *
     * @param manifest the manifest
     */
    @Override
    public void execute(Manifest manifest) throws Exception {

        log.trace("getting helm chart");
        var chart = templateGateway.getChart(manifest.template().id())
                .orElseThrow(TemplateNotFoundException::new);

        log.trace("creating values file");
        var valuesFile = createValuesFile(manifest);

        log.trace("creating helm template");
        try (var inputStream = HelmBuilder.init().template(manifest.release().id(), chart).namespace(manifest.namespace().id()).values(valuesFile).execute()) {
            manifestGateway.createManifest(inputStream, manifest.project().id(), manifest.release().version(), manifest.getFile());
        }

        log.trace("cleaning up old revisions");
        cleanRevisions(manifest);

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
                ReleaseStatus.PENDING_UPGRADE);
    }

    /**
     * Create a values file for the release
     *
     * @param manifest the manifest
     * @return the values file
     * @throws RuntimeException if an error occurs
     */
    private Path createValuesFile(Manifest manifest) throws RuntimeException {
        try {
            try (HelmValuesMerger merger = new HelmValuesMerger()) {

                // TODO: overwrite overlay values -> prefixed variables to dynamic values (like: $PROJECT_ID to project.id())

                // merge values
                merger.merge(manifest);

                // write values to file
                return merger.write(manifest.release().id());
            }
        } catch (IOException e) {
            log.error("failed to create values file", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Clean up old revisions of the release
     *
     * @param manifest the manifest
     */
    private void cleanRevisions(Manifest manifest) {
        try {
            // get revisions and current release
            var filesToKeep = manifest.release().revisions().stream().map(revision -> revision.version() + YAML_EXT).collect(Collectors.toList());
            filesToKeep.add(manifest.release().version() + YAML_EXT);
            log.trace("files to keep: {}", filesToKeep);

            // iterate through the list of files and delete the ones that are not in the list
            try (var files = Files.list(manifest.getFilePath())) {
                for (Path file : files.toList()) {
                    if (!filesToKeep.contains(file.getFileName().toString())) {
                        log.trace("deleting file: {}", file);
                        Files.delete(file);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("failed to clear revisions: {}", e.getMessage());
        }
    }


}
