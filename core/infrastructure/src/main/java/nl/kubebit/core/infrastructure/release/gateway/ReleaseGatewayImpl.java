package nl.kubebit.core.infrastructure.release.gateway;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseRef;
import nl.kubebit.core.entities.release.gateway.ReleaseGateway;
import nl.kubebit.core.infrastructure.release.datasource.ReleaseMapper;
import nl.kubebit.core.infrastructure.release.datasource.ReleaseRepository;

import nl.kubebit.core.usecases.common.annotation.Gateway;

/**
 * 
 */
@Gateway
public class ReleaseGatewayImpl implements ReleaseGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ReleaseRepository repository;

    /**
     * 
     * @param release
     */
    public ReleaseGatewayImpl(ReleaseRepository release) {
        this.repository = release;
    }

    /**
     * 
     */
    @Override
    public List<Release> findAll(String namespaceId) {
        log.trace("{} -> fetch releases", namespaceId);
        return repository.findAllInNamespace(namespaceId).stream().map(ReleaseMapper::toEntity).toList();
    }

    /**
     * 
     */
    @Override
    public Optional<Release> findById(String namespaceId, String releaseId) {
        log.trace("{} -> get release: {}", namespaceId, releaseId);
        return repository.findById(namespaceId, releaseId).map(ReleaseMapper::toEntity);
    }

    // -------
    
    /**
     * 
     */
    @Override
    public Optional<Release> create(Release release) {
        log.trace("save release: {} - v{}", release.id(), release.version());
        return repository.save(ReleaseMapper.toSchema(release)).map(ReleaseMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public Optional<Release> update(Release release) {
        log.trace("update release: {} - v{}", release.id(), release.version());
        return repository.update(ReleaseMapper.toSchema(release)).map(ReleaseMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public Optional<Release> patch(Release release) {
        log.trace("patch release status: {} - v{}", release.id(), release.version());
        return repository.patch(ReleaseMapper.toSchema(release)).map(ReleaseMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public void delete(Release release) {
        log.trace("delete release: {}", release.id());
        repository.delete(ReleaseMapper.toSchema(release)).ifPresent(l -> l.forEach(status -> log.error("delete release: {} -> {}", release.id(), status)));
    }

    // -------

    /**
     * 
     */
    @Override
    public List<ReleaseRef> findRevisions(String namespaceId, Release release) {
        log.trace("{} -> find release revisions", release.id());
        return repository.findRevisions(namespaceId, release.id()).stream().map(ReleaseMapper::toEntity).toList();
    }

    /**
     * 
     */
    @Override
    public Optional<ReleaseRef> findRevisionById(String namespaceId, Release release, Long revisionVersion) {
        log.trace("{} -> find release revision: {}", release.id(), revisionVersion);
        return repository.findRevision(namespaceId, release.id(), revisionVersion).map(ReleaseMapper::toEntity);
    }

    /**
     *
     */
    @Override
    public boolean unique(String namespaceId, String releaseId) {
        return repository.findById(namespaceId, releaseId).isEmpty();
    }


}
