package nl.kubebit.core.usecases.template.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
@Disabled
public class HelmChartUtilsTest {
    // --------------------------------------------------------------------------------------------

    /**
     * 
     */
    @Test
    @SuppressWarnings("unchecked")
    public void test() throws IOException {
        Map<String, Object> result = new ObjectMapper().readValue(new File("./src/test/resources/values.schema.json"), Map.class);
        System.out.println(result);
    }

    /**
     *
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSaveChartFile() throws IOException {
        // Prepare test data
        File tarPath = new File("./src/test/resources/wordpress-23.1.0.tgz");
        var destination = Files.createTempFile("", "").toFile();

        String fileName = "values.schema.json";


    }
}