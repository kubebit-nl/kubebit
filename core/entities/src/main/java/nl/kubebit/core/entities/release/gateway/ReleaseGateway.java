package nl.kubebit.core.entities.release.gateway;

import java.util.List;
import java.util.Optional;

import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseRef;

/**
 * 
 */
public interface ReleaseGateway {
    
    List<Release> findAll(String enviromentId);

    Optional<Release> findById(String enviromentId, String releaseId);

    // -------

    Optional<Release> create(Release release);

    Optional<Release> update(Release release);

    Optional<Release> patch(Release release);

    void delete(Release release);

    // -------

    List<ReleaseRef> findRevisions(String enviromentId, Release release);

    Optional<ReleaseRef> findRevisionById(String enviromentId, Release release, Long revisionVersion);
    
}
