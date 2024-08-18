package nl.kubebit.core.usecases.release.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 */
public class JsonSchemaParser {
    // --------------------------------------------------------------------------------------------
    
    /**
     * 
     * @param properties
     */
    @SuppressWarnings("unchecked")
    public void printPropertiesKeys(Map<String, Object> properties, int level) {
        properties.forEach((k, v) -> {
            System.out.println("-".repeat(level) + "-> " + k);
            if (v instanceof Map) {                
                printPropertiesKeys((Map<String, Object>) v, level + 1);               
            }
        });
    }

    /**
     * 
     */
    public Map<String, Object> getPropertiesKeys(Map<String, Object> schema) {
        Map<String, Object> propertiesKeys = new LinkedHashMap<>();
        propertiesKeysIterator(propertiesKeys, schema);
        return propertiesKeys;
    }

    /**
     * 
     * @param properties
     * @param schema
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void propertiesKeysIterator(Map<String, Object> properties, Map<String, Object> schema) {
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
