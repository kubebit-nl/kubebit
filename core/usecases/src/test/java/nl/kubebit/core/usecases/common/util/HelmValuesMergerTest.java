package nl.kubebit.core.usecases.common.util;

import nl.kubebit.core.entities.namespace.Namespace;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.entities.release.Manifest;
import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.template.Template;

import org.junit.jupiter.api.*;
import org.yaml.snakeyaml.Yaml;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class HelmValuesMergerTest {

    //
    private Manifest manifest;
    private Project project;
    private Namespace namespace;
    private Template template;
    private Release release;

    /**
     *
     */
    @BeforeEach
    void setUp() {
        manifest = mock(Manifest.class);
        project = mock(Project.class);
        namespace = mock(Namespace.class);
        template = mock(Template.class);
        release = mock(Release.class);
    }

    /**
     * Merge the values
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    void merge_when_then() throws IOException {
        var yaml = new Yaml();

        //
        Map<String, Object> templateValues;
        try (var templateStream = getClass().getClassLoader().getResourceAsStream("merge/template-values.yaml")) {
            templateValues = yaml.load(templateStream);
        }

        //
        String baseValues;
        try (var baseStream = getClass().getClassLoader().getResourceAsStream("merge/base-values.yaml")) {
            baseValues = new String(baseStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        //
        String stagingValues;
        try (var stagingStream = getClass().getClassLoader().getResourceAsStream("merge/staging-values.yaml")) {
            stagingValues = new String(stagingStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        //
        Map<String, Object> releaseValues;
        try (var releaseStream = getClass().getClassLoader().getResourceAsStream("merge/release-values.yaml")) {
            releaseValues = yaml.load(releaseStream);
        }

        //
        when(manifest.project()).thenReturn(project);
        String projectID = "819907ab-50e8-45fb-b687-f84067fed234";
        when(project.id()).thenReturn(projectID);

        when(manifest.namespace()).thenReturn(namespace);
        when(namespace.isProduction()).thenReturn(false);

        when(manifest.template()).thenReturn(template);
        when(template.values()).thenReturn(templateValues);
        when(template.baseValues()).thenReturn(baseValues);
        when(template.stagingValues()).thenReturn(stagingValues);

        when(manifest.release()).thenReturn(release);
        when(release.values()).thenReturn(releaseValues);

        //
        try (HelmValuesMerger helmValuesMerger = new HelmValuesMerger()) {
            helmValuesMerger.merge(manifest);

            //
            Map<String, Object> result = helmValuesMerger.getValues();

            //
            assertEquals(true, result.get("boolean"));
            assertEquals(43, result.get("number"));
            assertEquals(6.28, result.get("float"));
            assertEquals("hello $PROJECT_ID", result.get("string"));

            //
            assertEquals(3, ((ArrayList<?>) result.get("array")).size());
            assertEquals(6, ((ArrayList<?>) result.get("array")).get(0));
            assertEquals(7, ((ArrayList<?>) result.get("array")).get(1));
            assertEquals("$PROJECT_ID", ((ArrayList<?>) result.get("array")).get(2));

            //
            assertEquals(3, ((Map<?, ?>) result.get("object")).size());
            assertEquals("new value1", ((Map<?, ?>) result.get("object")).get("key1"));
            assertEquals("value2", ((Map<?, ?>) result.get("object")).get("key2"));
            assertEquals("new value3", ((Map<?, ?>) result.get("object")).get("key3"));

            //
            assertEquals(3, ((Map<?, ?>) result.get("nested")).size());
            assertEquals("value1", ((Map<?, ?>) result.get("nested")).get("key1"));
            assertEquals("value2 $PROJECT_ID", ((Map<?, ?>) result.get("nested")).get("key2"));

            //
            final String dynamicValue = "dynamic";
            helmValuesMerger.replace(manifest, Map.of("$DYNAMIC_VALUE", dynamicValue));
            result = helmValuesMerger.getValues();

            //
            assertEquals("hello " + projectID, result.get("string"));
            assertEquals(dynamicValue, result.get("dynamic"));
            assertEquals("value2 " + projectID, ((Map<?, ?>) result.get("nested")).get("key2"));

            //
            assertEquals(projectID, ((ArrayList<?>) result.get("array")).get(2));
            assertTrue(((ArrayList<String>) result.get("extra")).get(1).contains(projectID));
            assertTrue(((ArrayList<String>) result.get("extra")).get(2).contains(projectID));
        }

    }


}