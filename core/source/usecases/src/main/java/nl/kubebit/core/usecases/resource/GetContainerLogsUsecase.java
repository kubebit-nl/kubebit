package nl.kubebit.core.usecases.resource;

/**
 * 
 */
public interface GetContainerLogsUsecase {

    String execute(String projectId, String enviromentName, String podName, String containerName);
    
}
