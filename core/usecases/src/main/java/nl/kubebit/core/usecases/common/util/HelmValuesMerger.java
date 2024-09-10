package nl.kubebit.core.usecases.common.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import nl.kubebit.core.entities.release.Manifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.yaml.snakeyaml.Yaml;

/**
 *
 */
public class HelmValuesMerger implements AutoCloseable {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(HelmValuesMerger.class);

    //
    Map<String, Object> values;

    /**
     * Merge the values of the chart, base, staging and production values
     *
     * @param manifest the manifest
     */
    public void merge(Manifest manifest) {
        log.trace("merging values...");
        values = new HashMap<>(manifest.template().values());

        // merge overlay (unknown/new values will be copied)
        if (manifest.template().baseValues() != null)
            putKnown(values, manifest.template().baseValues());

        //
        if(manifest.namespace().isProduction()) {
            if (manifest.template().productionValues() != null)
                putKnown(values, manifest.template().productionValues());
        } else {
            if (manifest.template().stagingValues() != null)
                putKnown(values, manifest.template().stagingValues());
        }

        // merge input (unknown/new values will not be copied)
        if (manifest.release() != null && manifest.release().values() != null)
            putKnown(values, manifest.release().values());
    }


    /**
     * Combine the values of the chart, overlay and input values and write them to a file
     *
     * @param releaseId the release id
     * @return the path to the file
     * @throws IOException           if an I/O error occurs
     * @throws IllegalStateException if values are not merged
     */
    public Path write(String releaseId) throws IOException, IllegalStateException {
        var output = Files.createTempFile(releaseId + "-", "-values.yaml");
        log.trace("writing values to file -> {}", output);
        if (values == null || values.isEmpty()) {
            throw new IllegalStateException("values not merged");
        }
        final Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(output.toFile())) {
            yaml.dump(values, writer);
        }
        return output;
    }

    /**
     * Close the HelmValuesMerger
     */
    @Override
    public void close() {
        log.trace("closed");
    }

    // ------------------------------------------------------------------------

    /**
     * Put known values from map2 into map1
     *
     * @param map1 result map
     * @param map2 source map
     */
    @SuppressWarnings("unchecked")
    private void putKnown(Map<String, Object> map1, Map<String, Object> map2) {
        for (var entry : map2.entrySet()) {
            var key = entry.getKey();
            if (map1.containsKey(key)) {
                if (map1.get(key) instanceof Map && map2.get(key) instanceof Map) {
                    Map<String, Object> v1 = (Map<String, Object>) map1.get(key);
                    Map<String, Object> v2 = (Map<String, Object>) map2.get(key);
                    putKnown(v1, v2);
                } else {
                    map1.put(key, map2.get(key));
                }
            }
        }
    }

}
