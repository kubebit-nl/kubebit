package nl.kubebit.core.usecases.project.admin;

import nl.kubebit.core.usecases.project.dto.ProjectResponse;

import java.util.List;

/**
 * 
 */
public interface FetchProjectsAdminUseCase {

    List<ProjectResponse> execute();
    
}
