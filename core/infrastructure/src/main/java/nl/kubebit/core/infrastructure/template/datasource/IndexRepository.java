package nl.kubebit.core.infrastructure.template.datasource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.ChartWrapper;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

import static nl.kubebit.core.usecases.common.vars.GlobalVars.CHARTS_LOCATION;
import static nl.kubebit.core.usecases.common.vars.GlobalVars.YAML_EXT;

/**
 * 
 */
@Repository
@Validated
public class IndexRepository {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final YAMLFactory factory;

    //
    public IndexRepository(YAMLFactory factory) {
        this.factory = factory;
    }

    /**
     *  Pull the index file from the repository
     * @param repositoryUrl the repository url
     * @return the index file
     * @throws IOException if the file does not exist
     */
    public File pullIndex(@NotBlank String repositoryUrl) throws IOException {
        log.trace("pull index: {}", repositoryUrl);
        var file = File.createTempFile("index", YAML_EXT);
        
        // create webclient
        var webclient = WebClient.builder()
                .baseUrl(repositoryUrl)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(true)))
                .build();

        // get the chart
        Flux<DataBuffer> dataBufferFlux = webclient.get()
                .uri("/index.yaml").retrieve().bodyToFlux(DataBuffer.class);

        // write buffer to file
        DataBufferUtils.write(dataBufferFlux, file.toPath(), StandardOpenOption.CREATE).block();

        return file;
    }

    /**
     * Pull the chart from the repository
     * @param chartUri the chart uri
     * @return the chart file
     */
    public File pullChart(@NotNull URI chartUri) {
        String chart = chartUri.getPath().substring(chartUri.getPath().lastIndexOf('/') + 1);
        log.trace("pull chart: {}", chart);
        Path path = Paths.get(CHARTS_LOCATION, chart);
        if (!Files.exists(path)) {
            var webclient = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(
                            HttpClient.create().followRedirect(true)))
                    .build();

            // get the chart
            Flux<DataBuffer> dataBufferFlux = webclient.get()
                    .uri(chartUri).retrieve().bodyToFlux(DataBuffer.class);

            // write buffer to file
            DataBufferUtils.write(dataBufferFlux, path, StandardOpenOption.CREATE).block();
        }
        return path.toFile();
    }

    /**
     * Get the chart from the local repository
     * @param templateId the template id
     * @return the chart file
     */
    public Optional<File> getChart(String templateId) {
        String chart = templateId + ".tgz";
        log.trace("get chart: {}", chart);
        Path path = Paths.get(CHARTS_LOCATION, chart);
        if (Files.exists(path)) {
            return Optional.of(path.toFile());
        }
        return Optional.empty();
    }

    /**
     * Parse the index file
     * @param file the index file
     * @return the chart wrapper
     * @throws IOException if the file does not exist
     */
    public ChartWrapper parseIndexFile(@NotNull File file) throws IOException {
        ChartWrapper wrapper = new ObjectMapper(factory).readValue(file, ChartWrapper.class);
        for (var entry : wrapper.entries().entrySet()) {
            log.trace("entry: {}", entry.getKey());
            var charts = entry.getValue();
            for (int i = 0; i < (Math.min(charts.size(), 5)); i++) {
                var chart = charts.get(i);
                log.trace("- chart: {} - v{}", chart.name(), chart.version());
            }
        }
        return wrapper;
    }

    /**
     * Parse the yaml file
     * @param yamlFile the yaml file
     * @return the map
     * @throws IOException if the file does not exist
     */
    public Map<String, Object> parseYaml(File yamlFile) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Files.newInputStream(yamlFile.toPath())) {
            return yaml.load(inputStream);
        }
    }

}
