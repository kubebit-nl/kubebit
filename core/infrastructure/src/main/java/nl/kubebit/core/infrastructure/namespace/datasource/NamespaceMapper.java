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
    public static final String LABEL_MANAGEDBY_VALUE = "Kubebit";

    //
    public static final String LABEL_PROJECT_KEY = "kubebit.io/project";
    public static final String LABEL_NAME_KEY = "kubebit.io/name";

    //
    public static final String ANNOTATION_DESC_KEY = "kubebit.io/description";
    public static final String ANNOTATION_DEFAULT_KEY = "kubebit.io/default";
    public static final String ANNOTATION_PRODUCTION_KEY = "kubebit.io/production";

    /**
     * Map the schema to the entity
     * @param schema The schema to map
     * @return The entity
     */
    public static Namespace toEntity(io.fabric8.kubernetes.api.model.Namespace schema) {
        return new Namespace(
            schema.getMetadata().getLabels().get(LABEL_NAME_KEY),
            schema.getMetadata().getAnnotations().get(ANNOTATION_DESC_KEY),
            schema.getMetadata().getLabels().get(LABEL_PROJECT_KEY),
            Boolean.parseBoolean(schema.getMetadata().getAnnotations().get(ANNOTATION_DEFAULT_KEY)),
            Boolean.parseBoolean(schema.getMetadata().getAnnotations().get(ANNOTATION_PRODUCTION_KEY)));
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

        //
        meta.setLabels(Map.of(
            LABEL_MANAGEDBY_KEY, LABEL_MANAGEDBY_VALUE,
            LABEL_PROJECT_KEY, entity.projectId(),
            LABEL_NAME_KEY, entity.name())
        );

        //
        meta.setAnnotations(Map.of(
            ANNOTATION_DESC_KEY, entity.description(),
            ANNOTATION_DEFAULT_KEY, String.valueOf(entity.isDefault()),
            ANNOTATION_PRODUCTION_KEY, String.valueOf(entity.isProduction()))
        );

        //
        var namespace = new io.fabric8.kubernetes.api.model.Namespace();
        namespace.setMetadata(meta);

        //
        return namespace;
    }

}
