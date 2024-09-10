package nl.kubebit.core.usecases.common.util;

import java.io.*;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.entities.template.exception.ChartFileNotFoundException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.yaml.snakeyaml.Yaml;

/**
 *
 */
public abstract class HelmChartUtils {
    // --------------------------------------------------------------------------------------------

    //
    private static final Logger log = LoggerFactory.getLogger(HelmChartUtils.class);


    /**
     * Get values.yaml from a helm chart
     *
     * @param tarFile the tar file
     * @return the values.yaml as a map
     * @throws IOException                if an I/O error occurs
     * @throws ChartFileNotFoundException if the values.yaml file is not found
     */
    public static Map<String, Object> getChartValues(@NotNull File tarFile) throws IOException, ChartFileNotFoundException {
        log.trace("getting values.yaml: {}", tarFile);
        try (var tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(tarFile)))) {
            TarArchiveEntry entry;
            while ((entry = tarInput.getNextEntry()) != null) {
                log.trace("- {}", entry.getName());
                if (entry.getName().endsWith("values.yaml")) {
                    final Yaml yaml = new Yaml();
                    return yaml.load(tarInput);
                }
            }
        }
        return Map.of();
    }

    /**
     * Get values.schema.json from a helm chart
     *
     * @param tarFile the tar file
     * @return the values.schema.json as a map
     * @throws IOException                if an I/O error occurs
     * @throws ChartFileNotFoundException if the values.schema.json file is not found
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getChartSchema(@NotNull File tarFile) throws IOException, ChartFileNotFoundException {
        log.trace("getting values.schema.json: {}", tarFile);
        try (var tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(tarFile)))) {
            TarArchiveEntry entry;
            while ((entry = tarInput.getNextEntry()) != null) {
                log.trace("- {}", entry.getName());
                if (entry.getName().endsWith("values.schema.json")) {
                    return (Map<String, Object>) new ObjectMapper().readValue(tarInput, Map.class);
                }
            }
        }
        return Map.of();
    }

}