package nl.kubebit.core.usecases.common.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 */
public abstract class JsonSchemaSanitizer {
    // --------------------------------------------------------------------------------------------

    /**
     * Sanitize the values based on the schema
     * @param schema Json schema
     * @param values Values to sanitize in yaml format
     * @return Sanitized values
     */
    public static Map<String, Object> execute(Map<String, Object> schema, Map<String, Object> values) {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Object> properties = getSchemaPropertiesKeys(schema);
        sanitizeRecursive(properties, values, result);
        return result;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Sanitize the values recursively
     * @param properties Schema properties keys
     * @param values Values to sanitize
     * @param sanitized Sanitized values
     */
    @SuppressWarnings("unchecked")
    private static void sanitizeRecursive(Map<String, Object> properties, Map<String, Object> values, Map<String, Object> sanitized) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (properties.containsKey(entry.getKey())) {
                if (entry.getValue() instanceof Map && properties.get(entry.getKey()) instanceof Map) {
                    Map<String, Object> nestedSanitizedValues = new LinkedHashMap<>();
                    sanitizeRecursive((Map<String, Object>) properties.get(entry.getKey()), (Map<String, Object>) entry.getValue(), nestedSanitizedValues);
                    sanitized.put(entry.getKey(), nestedSanitizedValues);
                } else {
                    sanitized.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    /**
     * Get the schema properties keys
     * @param schema Json schema
     * @return Schema properties keys
     */
    private static Map<String, Object> getSchemaPropertiesKeys(Map<String, Object> schema) {
        Map<String, Object> propertiesKeys = new LinkedHashMap<>();
        propertiesKeysIterator(propertiesKeys, schema);
        return propertiesKeys;
    }

    /**
     * Iterate over the schema properties keys
     * @param properties Properties keys
     * @param schema Json schema
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void propertiesKeysIterator(Map<String, Object> properties, Map<String, Object> schema) {
        schema.forEach((k, v) -> {
            if (v instanceof Map) {
                if(k.equals("properties")) {
                    propertiesKeysIterator(properties, (Map<String, Object>) v);
                } else {
                    if(!properties.containsKey(k)) {
                        properties.put(k, new LinkedHashMap<String, Object>());
                    }
                    if(((Map)v).containsKey("properties")) {                         
                        propertiesKeysIterator((Map<String, Object>) properties.get(k), (Map<String, Object>) v);
                    }
                }                
            }
        });
    }

}