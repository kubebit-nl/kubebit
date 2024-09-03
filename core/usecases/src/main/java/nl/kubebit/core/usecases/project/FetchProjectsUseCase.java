package nl.kubebit.core.usecases.project;

import java.util.List;

import nl.kubebit.core.usecases.project.dto.ProjectResponse;

/**
 * 
 */
public interface FetchProjectsUseCase {

    List<ProjectResponse> execute();
    
}
