package nl.kubebit.core.usecases.template.thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.kubebit.core.entities.template.Chart;
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
    private final String CHART_VALUES_FILE = "values.yaml";
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
            template = setChart(template, chart);

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
            var schemaPath = saveChartFile(tarFile, CHART_SCHEMA_FILE);
            Map<String, Object> schema = loadSchema(schemaPath);
            log.trace("schema: {}", schemaPath.getName());

            //
            var valuesPath = saveChartFile(tarFile, CHART_VALUES_FILE);
            Map<String, Object> values = loadValues(valuesPath);
            log.trace("values: {}", valuesPath.getName());

            //
            template = setSchemaAndValues(template, schema, values);

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

    // --------------------------

    /**
     * 
     * @param tarFile
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static File saveChartFile(File tarFile, String fileName) throws FileNotFoundException, IOException {
        var schemaPath = Files.createTempFile("", fileName).toFile();
        HelmChartUtils.saveChartFile(tarFile, schemaPath, fileName);
        return schemaPath;
    }

    /**
     * 
     * @param schemaFile
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> loadSchema(File schemaFile) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(schemaFile, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     * @param valuesFile
     * @return
     */
    private static Map<String, Object> loadValues(File valuesFile) {
        final Yaml yaml = new Yaml();
        try (var valuesReader = new FileReader(valuesFile)) {
            return yaml.load(valuesReader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }        
    }

    /**
     * 
     * @param chart
     * @return
     */
    private Template setChart(Template template, Chart chart) {
        return new Template(
            template.id(),
            template.chart(),
            template.version(),
            template.repository(),
            template.type(),
            template.category(),
            template.icon(),
            template.formSchema(),
            template.overlayValues(),
            template.status(),
            template.message(),
            template.chartSchema(),
            template.chartValues(),
            chart != null ? chart.appVersion() : null,
            chart != null ? chart.description() : null,
            chart != null ? chart.keywords() : null,
            template.enviromentId());
    }

    /**
     * 
     * @param schema
     * @return
     */
    private Template setSchemaAndValues(Template template, Map<String, Object> schema, Map<String, Object> values) {
        return new Template(
            template.id(),
            template.chart(),
            template.version(),
            template.repository(),
            template.type(),
            template.category(),
            template.icon(),
            template.formSchema(),
            template.overlayValues(),
            template.status(),
            template.message(),
            schema,
            values,
            template.appVersion(),
            template.description(),
            template.keywords(),
            template.enviromentId());
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
