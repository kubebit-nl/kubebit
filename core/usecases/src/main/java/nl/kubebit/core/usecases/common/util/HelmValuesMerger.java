package nl.kubebit.core.usecases.common.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.yaml.snakeyaml.Yaml;

/**
 * 
 */
public abstract class HelmValuesMerger {
    // --------------------------------------------------------------------------------------------
    
    //
    private static final Logger log = LoggerFactory.getLogger(HelmValuesMerger.class);

    /**
     * Combine the values of the chart, overlay and input values and write them to a file
     * @param chartValues the chart values
     * @param overlayValues the overlay values
     * @param inputValues input values
     * @param outputFile the output file
     * @throws IOException if an I/O error occurs
     */
    public static void execute(Map<String, Object> chartValues, Map<String, Object> overlayValues, Map<String, Object> inputValues, Path outputFile) throws IOException {
        Map<String, Object> result = merge(chartValues, overlayValues, inputValues);
        log.trace("writing values to file -> {}", outputFile);
        final Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(outputFile.toFile())) {
            yaml.dump(result, writer);
        }
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Merge the values of the chart, overlay and input values
     * @param chartValues the chart values
     * @param overlayValues the overlay values
     * @param inputValues the input values
     * @return the merged values
     */
    private static Map<String, Object> merge(Map<String, Object> chartValues, Map<String, Object> overlayValues, Map<String, Object> inputValues) {
        log.trace("merging values...");
        Map<String, Object> result = new HashMap<>(chartValues);

        // merge overlay (unknown/new values will be copied)
        if (overlayValues != null)
            result.putAll(overlayValues);

        // merge input (unknown/new values will not be copied)
        if (inputValues != null)
            putKnown(result, inputValues);

        return result;
    }

    /**
     * Put known values from map2 into map1
     * @param map1 result map
     * @param map2 source map
     */
    @SuppressWarnings("unchecked")
    private static void putKnown(Map<String, Object> map1, Map<String, Object> map2) {
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
