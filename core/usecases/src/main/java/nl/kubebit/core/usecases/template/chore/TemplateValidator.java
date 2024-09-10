package nl.kubebit.core.usecases.template.chore;

import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotUpdatedException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.event.ServerSendEventGateway;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;
import nl.kubebit.core.usecases.common.util.HelmChartUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URI;

/**
 *
 */
@Component
public class TemplateValidator {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final ServerSendEventGateway eventGateway;
    private final TemplateGateway gateway;

    /**
     * Constructor
     *
     * @param eventGateway server send event gateway
     * @param gateway      template gateway
     */
    public TemplateValidator(ServerSendEventGateway eventGateway, TemplateGateway gateway) {
        this.eventGateway = eventGateway;
        this.gateway = gateway;
    }

    /**
     * Validate a template
     *
     * @param template the template
     */
    @Async
    public void execute(Template template) {
        log.trace("validating template -> {}", template.id());
        File tarFile = null;
        try {

            // pull index file
            var indexFile = gateway.pullIndex(template.repository());
            log.trace("index: {}", indexFile.getAbsolutePath());

            // parse index file
            var index = gateway.parseIndexFile(indexFile);
            var charts = index.entries().get(template.chart());
            if (charts == null) {
                throw new TemplateNotFoundException();
            }

            // get chart
            String version = template.version();
            var chart = charts.stream().filter(c -> c.version().equals(version)).findFirst().orElse(null);
            if (chart == null) {
                throw new TemplateNotFoundException();
            }

            //
            URI url = new URI(chart.urls().getFirst());
            if (!url.isAbsolute()) {
                url = new URI(template.repository() + "/" + chart.urls().getFirst());
            }
            log.trace("url: {}", url);

            // pull chart tar
            tarFile = gateway.pullChart(url);
            log.trace("tar: {}", tarFile.getAbsolutePath());

            //
            var entity = new Template(
                    template.id(),
                    chart.name(),
                    version,
                    template.repository(),
                    template.type(),
                    template.category(),
                    template.icon(),
                    template.schema(),
                    template.baseValues(),
                    template.stagingValues(),
                    template.productionValues(),
                    TemplateStatus.VALIDATED,
                    null,
                    HelmChartUtils.getChartValues(tarFile),
                    chart.appVersion(),
                    chart.description(),
                    chart.keywords(),
                    template.namespaceId());

            // update template
            gateway.update(entity);

            // update status of template
            var event = gateway.updateStatus(entity)
                    .map(TemplateResponse::new)
                    .orElseThrow(TemplateNotUpdatedException::new);

            // send server side event
            eventGateway.sendEvent(event);

        } catch (Exception e) {
            log.warn("failed to validate chart: {}", e.getMessage());
            if (gateway.updateStatus(template.setStatus(TemplateStatus.UNVALID, e.getMessage())).isEmpty()) {
                log.error("--> failed to update template status");
            }
            deleteFile(tarFile);
        }
        log.trace("validating finished");
    }

    // --------------------------------------------------------------------------------------------
    // private methods

    /**
     * Delete file
     *
     * @param file file
     */
    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            if (!file.delete()) {
                log.error("failed to delete file: {}", file.getAbsolutePath());
            }
        }
    }
}
