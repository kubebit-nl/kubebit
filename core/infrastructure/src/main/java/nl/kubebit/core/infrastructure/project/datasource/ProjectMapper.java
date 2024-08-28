package nl.kubebit.core.infrastructure.project.datasource;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import nl.kubebit.core.entities.project.Project;
import nl.kubebit.core.infrastructure.project.schema.ProjectCRD;
import nl.kubebit.core.infrastructure.project.schema.ProjectCRDSpec;

/**
 * 
 */
public abstract class ProjectMapper {
    // --------------------------------------------------------------------------------------------

    /**
     *
     */
    public static Project toEntity(ProjectCRD schema) {
        return new Project(
            schema.getMetadata().getName(), 
            schema.getSpec().name(), 
            schema.getSpec().description());
    }

    /**
     *
     */
    public static ProjectCRD toSchema(Project entity) {

        //
        var meta = new ObjectMeta();
        meta.setName(entity.id());

        //
        var spec = new ProjectCRDSpec(
            entity.name(), 
            entity.description());

        //
        var schema = new ProjectCRD();
        schema.setMetadata(meta);
        schema.setSpec(spec);

        //
        return schema;
    }
}
