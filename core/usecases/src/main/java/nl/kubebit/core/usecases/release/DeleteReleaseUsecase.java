package nl.kubebit.core.usecases.release;

/**
 * 
 */
public interface DeleteReleaseUsecase {
    
    void execute(String projectId, String enviromentName, String releaseId);
    
}
