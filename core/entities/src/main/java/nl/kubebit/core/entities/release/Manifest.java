package nl.kubebit.core.entities.release;

import nl.kubebit.core.entities.common.vars.GlobalVars;
import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.template.Template;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

/**
 *
 */
public record Manifest(

        Project project,
        Namespace namespace,
        Template template,
        Release release

) {
    // --------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param project   the project
     * @param namespace the namespace
     * @param template  the template
     * @param release   the release
     */
    public Manifest {

        // check the parameters
        requireNonNull(project);
        requireNonNull(namespace);
        requireNonNull(release);

        // create the manifest folder
        try {
            Files.createDirectories(getFolder(project, namespace, release));
        } catch (Exception e) {
            throw new RuntimeException("error creating manifest path: " + e.getMessage());
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Get the file
     *
     * @return the file
     */
    public File getFile() {
        return getFilePath().toFile();
    }

    /**
     * Get the file path
     *
     * @return the file path
     */
    public Path getFilePath() {
        return Paths.get(getFolder().toString(), getFileName());
    }

    /**
     * Get the file id
     *
     * @return the file id
     */
    public String getFileName() {
        return release().version() + GlobalVars.YAML_EXT;
    }

    /**
     * Get the folder
     *
     * @return the folder
     */
    public Path getFolder() {
        return getFolder(project().id(), namespace().name(), release().id());
    }

    /**
     * Get the folder
     *
     * @param project   the project
     * @param namespace the namespace
     * @param release   the release
     * @return the folder
     */
    private Path getFolder(Project project, Namespace namespace, Release release) {
        return getFolder(project.id(), namespace.name(), release.id());
    }

    /**
     * Get the folder
     *
     * @param projectId     the project id
     * @param namespaceName the namespace name
     * @param releaseId     the release id
     * @return the folder
     */
    private Path getFolder(String projectId, String namespaceName, String releaseId) {
        return Paths.get(
                GlobalVars.MANIFESTS_LOCATION, projectId, namespaceName, releaseId);
    }
}
