package nl.kubebit.core.usecases.common.util;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.release.Release;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static nl.kubebit.core.usecases.common.vars.GlobalVars.MANIFESTS_LOCATION;

/**
 *
 */
public abstract class ChoreUtils {
    // --------------------------------------------------------------------------------------------

    /**
     * Create the manifest path
     *
     * @param project   the project
     * @param namespace the namespace
     * @param release   the release
     * @return the manifest path
     * @throws RuntimeException if an error occurs
     */
    public static Path createManifestPath(Project project, Namespace namespace, Release release) throws RuntimeException {
        try {
            var manifestPath = Paths.get(MANIFESTS_LOCATION, project.id(), namespace.name(), release.id());
            Files.createDirectories(manifestPath);
            return manifestPath;
        } catch (Exception e) {
            throw new RuntimeException("error creating manifest path: " + e.getMessage());
        }
    }

}
