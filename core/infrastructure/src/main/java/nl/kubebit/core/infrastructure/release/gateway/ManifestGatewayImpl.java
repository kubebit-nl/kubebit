package nl.kubebit.core.infrastructure.release.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import io.fabric8.kubernetes.client.KubernetesClient;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseStatus;
import nl.kubebit.core.entities.release.exception.ReleaseNotUpdatedException;
import nl.kubebit.core.entities.release.gateway.ManifestGateway;
import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.infrastructure.release.datasource.ManifestRepository;
import nl.kubebit.core.infrastructure.template.gateway.TemplateGatewayImpl;
import nl.kubebit.core.usecases.common.annotation.Gateway;

/**
 * 
 */
@Gateway
public class ManifestGatewayImpl implements ManifestGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ReleaseGatewayImpl releaseGateway;
    private final TemplateGatewayImpl templateGateway;
    private final ManifestRepository manifestRepository;
    
    /**
     * 
     * @param releaseGateway
     * @param templateGateway
     * @param manifestRepository
     */
    public ManifestGatewayImpl(
            ReleaseGatewayImpl releaseGateway, 
            TemplateGatewayImpl templateGateway,
            ManifestRepository manifestRepository,
            KubernetesClient kubernetes) {
        this.releaseGateway = releaseGateway;
        this.templateGateway = templateGateway;
        this.manifestRepository = manifestRepository;
    }

    /**
     * 
     */
    @Async
    @Override    
    public void installManifest(String projectId, String envirmentName, Release release, Template template) {
        log.info("{} -> installing template: {}", release.enviromentId(), template.id());
        try {
            
            // set pending status
            releaseGateway.patch(release).orElseThrow(() -> new ReleaseNotUpdatedException());

            //
            var chart = templateGateway.getChart(template.id()).orElseThrow(() -> new TemplateNotFoundException(template.id()));
            log.trace("chart: {}", chart.getAbsolutePath());

            // patch manifest
            var resources = manifestRepository.installManifest(projectId, envirmentName, release, template, chart);

            // set deployed status
            releaseGateway.patch(release.setDeployedAndResources(resources))
                .orElseThrow(() -> new ReleaseNotUpdatedException());  

        } catch (Exception e) {
            log.warn("{} -> error installing template: {}", release.enviromentId(), template.id(), e);
            if(releaseGateway.patch(release.setStatus(ReleaseStatus.FAILED, e.getMessage())).isEmpty()) {
                log.error("--> failed to update release status");
            }
        }
        log.trace("thread finished");
    }

    /**
     * 
     * @param projectId
     * @param envirmentName
     * @param release
     * @param template
     */
    @Async
    @Override    
    public void patchManifest(String projectId, String envirmentName, Release release) {
        log.info("{} -> patching manifest: {}", release.enviromentId());
        try {
            
            // set pending status
            releaseGateway.patch(release).orElseThrow(() -> new ReleaseNotUpdatedException());

            // patch manifest
            manifestRepository.patchManifest(projectId, envirmentName, release);

            // set deployed status
            releaseGateway.patch(release.setStatus(ReleaseStatus.DEPLOYED, null))
                .orElseThrow(() -> new ReleaseNotUpdatedException());  

        } catch (Exception e) {
            log.warn("{} -> error patching manifest: {}", release.enviromentId(), release.id(), e);
            if(releaseGateway.patch(release.setStatus(ReleaseStatus.FAILED, e.getMessage())).isEmpty()) {
                log.error("--> failed to update release status");
            }
        }
        log.trace("thread finished");
    }
    
}
