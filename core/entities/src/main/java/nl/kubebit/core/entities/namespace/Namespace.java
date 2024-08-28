package nl.kubebit.core.entities.namespace;

/**
 * 
 */
public record Namespace(

    String name,
    String description,
    String projectId,
    boolean isDefault

) {

    /**
     *
     */
    public String id() {
        return formatId(projectId, name);
    }

    /**
     *
     */
    public static String formatId(String projectId, String environmentName) {
        return String.format("%s-%s", projectId, environmentName);
    }
}
