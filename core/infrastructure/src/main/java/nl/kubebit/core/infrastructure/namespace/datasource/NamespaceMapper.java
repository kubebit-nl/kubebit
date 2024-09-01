package nl.kubebit.core.infrastructure.namespace.datasource;

import java.util.Map;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import nl.kubebit.core.entities.namespace.Namespace;

/**
 * 
 */
public abstract class NamespaceMapper {
    // --------------------------------------------------------------------------------------------

    //
    public static final String LABEL_MANAGEDBY_KEY = "app.kubernetes.io/managed-by";
    public static final String LABEL_MANAGEDBY_VALUE = "KubeBit";

    //
    public static final String LABEL_PROJECT_KEY = "kubebit.io/projects";
    public static final String LABEL_DEFAULT_KEY = "kubebit.io/default";

    //
    public static final String ANNOTATION_NAME_KEY = "kubebit.io/name";
    public static final String ANNOTATION_DESCRIPTION_KEY = "kubebit.io/description";

    /**
     * Map the schema to the entity
     * @param schema The schema to map
     * @return The entity
     */
    public static Namespace toEntity(io.fabric8.kubernetes.api.model.Namespace schema) {
        return new Namespace(
            schema.getMetadata().getAnnotations().get(ANNOTATION_NAME_KEY), 
            schema.getMetadata().getAnnotations().get(ANNOTATION_DESCRIPTION_KEY), 
            schema.getMetadata().getLabels().get(LABEL_PROJECT_KEY),
            Boolean.parseBoolean(schema.getMetadata().getLabels().get(LABEL_DEFAULT_KEY)));
    }

    /**
     *
     * @param entity The entity to map
     * @return The schema
     */
    public static io.fabric8.kubernetes.api.model.Namespace toSchema(Namespace entity) {

        //
        var meta = new ObjectMeta();
        meta.setName(entity.id());
        meta.setLabels(Map.of(
            LABEL_MANAGEDBY_KEY, LABEL_MANAGEDBY_VALUE,
            LABEL_PROJECT_KEY, entity.projectId(),
            LABEL_DEFAULT_KEY, String.valueOf(entity.isDefault())));
        meta.setAnnotations(Map.of(
            ANNOTATION_NAME_KEY, entity.name(),
            ANNOTATION_DESCRIPTION_KEY, entity.description()));

        //
        var namespace = new io.fabric8.kubernetes.api.model.Namespace();
        namespace.setMetadata(meta);

        //
        return namespace;
    }

}
