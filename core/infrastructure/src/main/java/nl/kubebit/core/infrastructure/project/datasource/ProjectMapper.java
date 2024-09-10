package nl.kubebit.core.infrastructure.project.datasource;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.infrastructure.project.schema.ProjectCRD;
import nl.kubebit.core.infrastructure.project.schema.ProjectCRDSpec;

/**
 * Utility class for mapping between Project entity and ProjectCRD schema.
 */
public abstract class ProjectMapper {
    // --------------------------------------------------------------------------------------------

    /**
     * Converts a ProjectCRD schema object to a Project entity.
     *
     * @param schema the ProjectCRD schema object
     * @return the corresponding Project entity
     */
    public static Project toEntity(ProjectCRD schema) {
        return new Project(
                schema.getMetadata().getName(),
                schema.getSpec().name(),
                schema.getSpec().description());
    }

    /**
     * Converts a Project entity to a ProjectCRD schema object.
     *
     * @param entity the Project entity
     * @return the corresponding ProjectCRD schema object
     */
    public static ProjectCRD toSchema(Project entity) {

        // Create and set metadata
        var meta = new ObjectMeta();
        meta.setName(entity.id());

        // Create and set specification
        var spec = new ProjectCRDSpec(
                entity.name(),
                entity.description());

        // Create and set schema
        var schema = new ProjectCRD();
        schema.setMetadata(meta);
        schema.setSpec(spec);

        // Return the schema object
        return schema;
    }
}
