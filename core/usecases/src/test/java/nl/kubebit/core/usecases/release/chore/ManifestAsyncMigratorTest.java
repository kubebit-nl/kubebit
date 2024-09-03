package nl.kubebit.core.usecases.release.chore;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.kubebit.core.usecases.common.util.HelmBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
@Disabled
class ManifestAsyncMigratorTest {

    /**
     * USER-SUPPLIED VALUES:
     * veld: waarde
     */
    @Test
    void test() {
        try (var inputStream = HelmBuilder.init().additionalArgs("get", "values", "busybox").namespace("2c70027e-helm").execute()) {
            Map<String, Object> values = new Yaml().load(inputStream);
            values.remove("USER-SUPPLIED VALUES");
            System.out.println(values);

        } catch (Exception e) {
            fail(e);
        }
    }

    /**
     *
     */
    @Test
    void test2() {
        assertThrows(RuntimeException.class, () -> {
            try (var inputStream = HelmBuilder.init().additionalArgs("get", "values", "unknown").namespace("2c70027e-helm").execute()) {
            }
        });
    }

    /**
     *
     */
    @Test
    void test3() {
        assertThrows(RuntimeException.class, () -> {
            try (var inputStream = HelmBuilder.init().additionalArgs("get", "values").namespace("2c70027e-helm").execute()) {
            }
        });
    }
}