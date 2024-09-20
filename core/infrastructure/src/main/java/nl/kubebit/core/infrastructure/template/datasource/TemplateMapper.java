package nl.kubebit.core.infrastructure.template.datasource;

import java.util.Map;

import io.fabric8.kubernetes.api.model.AnyType;
import io.fabric8.kubernetes.api.model.ObjectMeta;

import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.infrastructure.template.schema.TemplateCRD;
import nl.kubebit.core.infrastructure.template.schema.TemplateCRDSpec;
import nl.kubebit.core.infrastructure.template.schema.TemplateCRDStatus;

/**
 * 
 */
public abstract class TemplateMapper {
    // --------------------------------------------------------------------------------------------

    /**
     *
     */
    public static Template toEntity(TemplateCRD schema) {
        return new Template(
            schema.getMetadata().getName(),
            schema.getSpec().chart(),            
            schema.getSpec().version(),
            schema.getSpec().repository(),
            schema.getSpec().type(),
            schema.getSpec().category(),
            schema.getSpec().icon(),
            schema.getSpec().schema() != null ? transformAnyType(schema.getSpec().schema()) : null,
            schema.getSpec().baseValues() != null ? schema.getSpec().baseValues().toString() : null,
            schema.getSpec().stagingValues() != null ? schema.getSpec().stagingValues().toString() : null,
            schema.getSpec().productionValues() != null ? schema.getSpec().productionValues().toString() : null,
            schema.getStatus() != null ? schema.getStatus().status() : TemplateStatus.UNKNOWN,
            schema.getStatus() != null ? schema.getStatus().message() : null,
            schema.getStatus() != null ? transformAnyType(schema.getStatus().values()) : null,
            schema.getStatus() != null ? schema.getStatus().appVersion() : null,
            schema.getStatus() != null ? schema.getStatus().description() : null,
            schema.getStatus() != null ? schema.getStatus().keywords() : null,
            schema.getMetadata().getNamespace());
    }

    

    /**
     *
     */
    public static TemplateCRD toSchema(Template entity) {

        //
        var meta = new ObjectMeta();
        meta.setName(entity.id());

        //
        var spec = new TemplateCRDSpec(
            entity.chart(),            
            entity.version(),
            entity.repository(),
            entity.type(),
            entity.category(),
            entity.icon(),
            transformAnyType(entity.schema()),
            transformAnyType(entity.baseValues()),
            transformAnyType(entity.stagingValues()),
            transformAnyType(entity.productionValues()));

        //
        var status = new TemplateCRDStatus(
            entity.status(), 
            entity.message(),
            transformAnyType(entity.values()),
            entity.appVersion(),
            entity.description(),
            entity.keywords());

        //
        var schema = new TemplateCRD();
        schema.setMetadata(meta);
        schema.setSpec(spec);
        schema.setStatus(status);

        //
        return schema;
    }


    /**
     *
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> transformAnyType(AnyType anyType) {
        return anyType == null ? null : (Map<String, Object>) anyType.getValue();
    }

    /**
     * Covert the map to AnyType
     * @param map the map
     * @return the AnyType
     */
    private static AnyType transformAnyType(Map<String, Object> map) {
        return map == null ? null : new AnyType(map);
    }

    /**
     *  Covert the string to AnyType
     * @param str the string
     * @return the AnyType
     */
    private static AnyType transformAnyType(String str) {
        return str == null ? null : new AnyType(str);
    }
}
