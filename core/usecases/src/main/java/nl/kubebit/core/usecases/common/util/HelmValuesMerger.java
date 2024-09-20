package nl.kubebit.core.usecases.common.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import nl.kubebit.core.entities.release.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.yaml.snakeyaml.Yaml;

import static nl.kubebit.core.usecases.release.chore.constants.OverlayConstants.OVERLAY_PROJECT_ID;

/**
 *
 */
public class HelmValuesMerger implements AutoCloseable {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(HelmValuesMerger.class);

    //
    private String releaseId;
    Map<String, Object> values;


    /**
     * Merge the values of the chart, base, staging and production values
     *
     * @param manifest the manifest
     */
    public void merge(Manifest manifest) {
        log.trace("merging values...");

        //
        releaseId = manifest.release().id();
        values = new HashMap<>(manifest.template().values());

        // merge overlay (unknown/new values will be copied)
        if (manifest.template().baseValues() != null)
            deepMerge(values, manifest.template().baseValues());

        //
        if (manifest.namespace().isProduction()) {
            if (manifest.template().productionValues() != null)
                deepMerge(values, manifest.template().productionValues());
        } else {
            if (manifest.template().stagingValues() != null)
                deepMerge(values, manifest.template().stagingValues());
        }

        // merge input (unknown/new values will not be copied)
        if (manifest.release() != null && manifest.release().values() != null)
            putKnown(values, manifest.release().values());
    }

    /**
     *
     */
    public void replace(Manifest manifest, Map<String, Object> dynamicProperties) {
        log.trace("replacing values...");
        Objects.requireNonNull(manifest, "manifest not given");
        Objects.requireNonNull(this.values, "values not merged");

        //
        Map<String, Object> replacements = new HashMap<>(dynamicProperties == null ? Collections.emptyMap() : dynamicProperties);
        replacements.put(OVERLAY_PROJECT_ID, manifest.project().id());

        //
        replaceProperties(this.values, replacements);
    }

    /**
     * Combine the values of the chart, overlay and input values and write them to a file
     *
     * @return the path to the file
     * @throws IOException           if an I/O error occurs
     * @throws IllegalStateException if values are not merged
     */
    public Path write() throws IOException, IllegalStateException {
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
     * Result mapping values
     *
     * @return the values
     */
    public Map<String, Object> getValues() {
        return values;
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

    /**
     * Deep merge map2 into map1
     *
     * @param map1 map1
     * @param map2 map2
     */
    @SuppressWarnings("unchecked")
    private void deepMerge(Map<String, Object> map1, Map<String, Object> map2) {
        for (var entry : map2.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            if (map1.containsKey(key)) {
                if (map1.get(key) instanceof Map && value instanceof Map) {
                    deepMerge((Map<String, Object>) map1.get(key), (Map<String, Object>) value);
                } else {
                    map1.put(key, value);
                }
            } else {
                map1.put(key, value);
            }
        }
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    private void replaceProperties(Map<String, Object> map, Map<String, Object> replacements) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            switch (value) {
                case String stringValue -> {
                    for (var replacement : replacements.entrySet()) {
                        if (stringValue.contains(replacement.getKey())) {
                            stringValue = stringValue.replace(replacement.getKey(), replacement.getValue().toString());
                        }
                    }
                    map.put(key, stringValue);
                }
                case ArrayList<?> arrayValue -> {
                    for (int i = 0; i < arrayValue.size(); i++) {
                        var item = arrayValue.get(i);
                        if (item instanceof String stringItem) {
                            for (var replacement : replacements.entrySet()) {
                                if (stringItem.contains(replacement.getKey())) {
                                    stringItem = stringItem.replace(replacement.getKey(), replacement.getValue().toString());
                                }
                            }
                            ((ArrayList<Object>) arrayValue).set(i, stringItem);
                        }
                    }
                }
                case Map<?, ?> mapValue -> replaceProperties((Map<String, Object>) value, replacements);
                case null, default -> log.trace("unhandled value type: {} -> {}", key, Optional.ofNullable(value)
                        .map(v -> v.getClass().getSimpleName()).orElse("null"));
            }
        }
    }
}


