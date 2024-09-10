package nl.kubebit.core.infrastructure.project.datasource;

import static org.junit.jupiter.api.Assertions.*;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import org.junit.jupiter.api.Test;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.infrastructure.project.schema.ProjectCRD;
import nl.kubebit.core.infrastructure.project.schema.ProjectCRDSpec;

/**
 * Unit tests for the ProjectMapper class.
 */
public class ProjectMapperTest {

    /**
     * Tests the toEntity method.
     */
    @Test
    void toEntityConvertsSchemaToEntity() {
        ProjectCRD schema = new ProjectCRD();
        schema.setMetadata(new ObjectMeta());
        schema.getMetadata().setName("test-id");
        schema.setSpec(new ProjectCRDSpec("test-name", "test-description"));

        Project entity = ProjectMapper.toEntity(schema);

        assertEquals("test-id", entity.id());
        assertEquals("test-name", entity.name());
        assertEquals("test-description", entity.description());
    }

    /**
     *
     */
    @Test
    void toEntityHandlesNullValues() {
        ProjectCRD schema = new ProjectCRD();
        schema.setMetadata(new ObjectMeta());
        schema.getMetadata().setName(null);
        schema.setSpec(new ProjectCRDSpec(null, null));

        Project entity = ProjectMapper.toEntity(schema);

        assertNull(entity.id());
        assertNull(entity.name());
        assertNull(entity.description());
    }

    @Test
    void toSchemaConvertsEntityToSchema() {
        Project entity = new Project("test-id", "test-name", "test-description");

        ProjectCRD schema = ProjectMapper.toSchema(entity);

        assertEquals("test-id", schema.getMetadata().getName());
        assertEquals("test-name", schema.getSpec().name());
        assertEquals("test-description", schema.getSpec().description());
    }

    @Test
    void toSchemaHandlesNullValues() {
        Project entity = new Project(null, null, null);

        ProjectCRD schema = ProjectMapper.toSchema(entity);

        assertNull(schema.getMetadata().getName());
        assertNull(schema.getSpec().name());
        assertNull(schema.getSpec().description());
    }
}