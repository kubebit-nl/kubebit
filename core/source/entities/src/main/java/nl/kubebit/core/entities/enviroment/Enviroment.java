package nl.kubebit.core.entities.enviroment;

/**
 * 
 */
public record Enviroment(

    String name,
    String description,
    String projectId

) {

    /**
     * 
     * @return
     */
    public String id() {
        return formatId(projectId, name);
    }

    /**
     * 
     * @param projectId
     * @param environmentName
     * @return
     */
    public static String formatId(String projectId, String environmentName) {
        return String.format("%s-%s", projectId, environmentName);
    }
}
