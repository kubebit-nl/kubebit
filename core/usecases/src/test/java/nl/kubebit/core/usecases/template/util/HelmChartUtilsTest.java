package nl.kubebit.core.usecases.template.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
@Disabled
public class HelmChartUtilsTest {
    // --------------------------------------------------------------------------------------------

    /**
     * @throws IOException
     * @throws DatabindException
     * @throws StreamReadException
     * 
     */
    @Test
    @SuppressWarnings("unchecked")
    public void test() throws StreamReadException, DatabindException, IOException {
        Map<String, Object> result = new ObjectMapper().readValue(new File("./src/test/resources/values.schema.json"),
                Map.class);
        System.out.println(result);
    }

    /**
     * 
     * @throws IOException
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSaveChartFile() throws IOException {
        // Prepare test data
        File tarPath = new File("./src/test/resources/wordpress-23.1.0.tgz");
        var destination = Files.createTempFile("", "").toFile();

        String fileName = "values.schema.json";

        // Call the method
        try {
            HelmChartUtils.saveChartFile(tarPath, destination, fileName);

            // Assert that the file was saved successfully
            assertTrue(destination.exists());

            Map<String, Object> result = new ObjectMapper().readValue(new File("./src/test/resources/values.schema.json"), Map.class);
            System.out.println(result);
            
            // Add more assertions if needed
        } catch (Exception e) {
            // Handle the exception if necessary
            fail("Exception should not be thrown");
        }
    }
}