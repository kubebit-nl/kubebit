package nl.kubebit.core.infrastructure.template.datasource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
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

import static nl.kubebit.core.infrastructure.configuration.StorageConfig.CHARTS_LOCATION;

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
    public final String GZIP_TAR = ".tgz";
    public final String YAML_EXT = ".yaml";

    //
    private final YAMLFactory factory;

    //
    public IndexRepository(YAMLFactory factory) {
        this.factory = factory;
    }

    /**
     * 
     * @param location
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public File pullIndex(@NotBlank String repositoryUrl) throws IOException, URISyntaxException {
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
     * 
     * @param chartUri
     * @return
     * @throws IOException
     */
    public File pullChart(@NotNull URI chartUri) throws IOException {
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
     * 
     * @param templateId
     * @return
     * @throws IOException 
     */
    public Optional<File> getChart(String templateId) throws IOException {
        String chart = templateId + GZIP_TAR;
        log.trace("pull chart: {}", chart);
        Path path = Paths.get(CHARTS_LOCATION, chart);
        if (Files.exists(path)) {
            return Optional.of(path.toFile());
        }
        return Optional.empty();
    }

    /**
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public ChartWrapper parseIndexFile(@NotNull File file) throws IOException {
        ChartWrapper wrapper = new ObjectMapper(factory).readValue(file, ChartWrapper.class);
        for (var entry : wrapper.entries().entrySet()) {
            log.trace("entry: {}", entry.getKey());
            var charts = entry.getValue();
            for (int i = 0; i < (charts.size() < 5 ? charts.size() : 5); i++) {
                var chart = charts.get(i);
                log.trace("- chart: {} - v{}", chart.name(), chart.version());
            }
        }
        return wrapper;
    }

    /**
     * 
     * @param yamlFile
     * @return
     * @throws IOException
     */
    public Map<String, Object> parseYaml(File yamlFile) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = Files.newInputStream(yamlFile.toPath())) {
            return yaml.load(inputStream);
        }
    }

}
