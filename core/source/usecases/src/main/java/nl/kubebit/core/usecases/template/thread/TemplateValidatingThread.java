package nl.kubebit.core.usecases.template.thread;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotUpdatedException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.template.util.HelmChartUtils;

/**
 * 
 */
public class TemplateValidatingThread extends Thread {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final String CHART_SCHEMA_FILE = "values.schema.json";

    //
    private final TemplateGateway gateway;
    private Template template;

    //
    public TemplateValidatingThread(TemplateGateway gateway, Template template) {
        this.gateway = gateway;
        this.template = template;
    }

    /**
     * 
     */
    @Override
    @SuppressWarnings("unchecked")
    public void run() {
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
                throw new TemplateNotFoundException(template.id());
            }

            //
            var chart = charts.stream().filter(c -> c.version().equals(template.version())).findFirst().orElse(null);
            if (chart == null) {
                throw new TemplateNotFoundException(template.id());
            }
            template = template.setChart(chart);

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
            var schemaPath = Files.createTempFile("", CHART_SCHEMA_FILE).toFile();
            HelmChartUtils.saveChartFile(tarFile, schemaPath, CHART_SCHEMA_FILE);
            Map<String, Object> schema = new ObjectMapper().readValue(schemaPath, Map.class);
            template = template.setSchema(schema);

            //
            gateway.updateStatus(template.setStatus(TemplateStatus.VALIDATED, null))
                    .orElseThrow(() -> new TemplateNotUpdatedException());

        } catch (Exception e) {
            log.warn("failed to validate chart: {}", e.getMessage());
            if (gateway.updateStatus(template.setStatus(TemplateStatus.UNVALID, e.getMessage())).isEmpty()) {
                log.error("--> failed to update template status");
            }
            deleteFile(tarFile);
        }
        log.trace("validating finished");
    }

    /**
     * 
     * @param file
     */
    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

}
