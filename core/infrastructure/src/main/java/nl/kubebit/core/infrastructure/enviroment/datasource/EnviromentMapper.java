package nl.kubebit.core.infrastructure.enviroment.datasource;

import java.util.Map;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import nl.kubebit.core.entities.enviroment.Enviroment;

/**
 * 
 */
public abstract class EnviromentMapper {
    // --------------------------------------------------------------------------------------------

    //
    public static String LABEL_MANAGEDBY_KEY = "app.kubernetes.io/managed-by";
    public static String LABEL_MANAGEDBY_VALUE = "KubeBit";

    //
    public static String LABEL_PROJECT_KEY = "kubebit.io/project";

    //
    public static String ANNOTATION_NAME_KEY = "kubebit.io/name";
    public static String ANNOTATION_DESCRIPTION_KEY = "kubebit.io/description";

    /**
     * 
     * @return
     */
    public static Enviroment toEntity(Namespace namespace) {
        return new Enviroment(
            namespace.getMetadata().getAnnotations().get(ANNOTATION_NAME_KEY), 
            namespace.getMetadata().getAnnotations().get(ANNOTATION_DESCRIPTION_KEY), 
            namespace.getMetadata().getLabels().get(LABEL_PROJECT_KEY));        
    }
    
    /**
     * 
     * @param enviroment
     * @return
     */
    public static Namespace toSchema(Enviroment enviroment) {

        //
        var meta = new ObjectMeta();
        meta.setName(enviroment.id());
        meta.setLabels(Map.of(
            LABEL_MANAGEDBY_KEY, LABEL_MANAGEDBY_VALUE,
            LABEL_PROJECT_KEY, enviroment.projectId()));
        meta.setAnnotations(Map.of(
            ANNOTATION_NAME_KEY, enviroment.name(),
            ANNOTATION_DESCRIPTION_KEY, enviroment.description()));

        //
        var namespace = new Namespace();
        namespace.setMetadata(meta);

        //
        return namespace;
    }




}
