package nl.kubebit.core.usecases.resource;

/**
 * 
 */
public interface GetContainerLogsUsecase {

    String execute(String projectId, String namespaceName, String podName, String containerName);
    
}
