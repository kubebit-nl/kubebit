package nl.kubebit.core.usecases.release.util;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 */
public class JsonSchemaSanitizerTest {
    // --------------------------------------------------------------------------------------------

    /**
     *
     */
    @Test
    @SuppressWarnings("unchecked")
    void sanitizer_whenUnknownValues_thenEmpty() throws IOException {
        try(var inputStream = getClass().getClassLoader().getResourceAsStream("values.schema.json")) {
            Map<String, Object> schema = new ObjectMapper().readValue(inputStream, Map.class);

            // when
            var result = JsonSchemaSanitizer.execute(schema, Map.of("key1", "value1", "key2", Map.of("key1", "value1", "key2", "value2")));

            // then
            assertEquals(0, result.size());
        }
    }

    /**
     *
     */
    @Test
    @SuppressWarnings("unchecked")
    void sanitizer_whenKnownValueInWrongLevel_thenEmpty() throws IOException {
        try(var inputStream = getClass().getClassLoader().getResourceAsStream("values.schema.json")) {
            Map<String, Object> schema = new ObjectMapper().readValue(inputStream, Map.class);

            // when
            var result = JsonSchemaSanitizer.execute(schema, Map.of("key1", "value1", "key2", Map.of("wordpressBlogName", "value1", "key2", "value2")));

            // then
            assertEquals(0, result.size());
        }
    }

    /**
     *
     */
    @Test
    @SuppressWarnings("unchecked")
    void sanitizer_whenKnowValue_thenOneValue() throws IOException {
        try(var inputStream = getClass().getClassLoader().getResourceAsStream("values.schema.json")) {
            Map<String, Object> schema = new ObjectMapper().readValue(inputStream, Map.class);

            // when
            var result = JsonSchemaSanitizer.execute(schema, Map.of("wordpressBlogName", "value1", "key2", Map.of("key1", "value1", "key2", "value2")));

            // then
            assertEquals(1, result.size());
            assertEquals("value1", result.get("wordpressBlogName"));
        }
    }


    /**
     *
     */
    @Test
    @SuppressWarnings("unchecked")
    void sanitizer_whenKnowValueSecondLevel_thenOneValue() throws IOException {
        try(var inputStream = getClass().getClassLoader().getResourceAsStream("values.schema.json")) {
            Map<String, Object> schema = new ObjectMapper().readValue(inputStream, Map.class);

            // when
            var result = JsonSchemaSanitizer.execute(schema, Map.of("key1", "value1", "mariadb", Map.of("enabled", false, "key2", "value2")));

            // then
            assertEquals(1, result.size());
            assertEquals(false, ((Map<String, Object>) result.get("mariadb")).get("enabled"));
        }
    }

}
