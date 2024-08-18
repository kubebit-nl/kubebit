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
     * 
     * @param chartValues
     * @param overlayValues
     * @param inputValues
     * @param outputFile
     * @throws IOException
     */
    public static void combine(Map<String, Object> chartValues, Map<String, Object> overlayValues, Map<String, Object> inputValues, Path outputFile) throws IOException {
        Map<String, Object> result = merge(chartValues, overlayValues, inputValues);

        log.trace("writing values to file -> {}", outputFile);
        final Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(outputFile.toFile())) {
            yaml.dump(result, writer);
        }
    }

    /**
     * 
     * @param chartValues
     * @param overlayValues
     * @param inputValues
     * @return
     */
    public static Map<String, Object> merge(Map<String, Object> chartValues, Map<String, Object> overlayValues, Map<String, Object> inputValues) {
        log.info("merging values");
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
     *
     * @param map1
     * @param map2
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
