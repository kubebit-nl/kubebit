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
            schema.getSpec().formSchema() != null ? transformAnyType(schema.getSpec().formSchema()) : null,
            schema.getSpec().overlayValues() != null ? transformAnyType(schema.getSpec().overlayValues()) : null,
            schema.getStatus() != null ? schema.getStatus().status() : TemplateStatus.UNKNOWN,
            schema.getStatus() != null ? schema.getStatus().message() : null,
            schema.getStatus() != null ? transformAnyType(schema.getStatus().chartSchema()) : null,
            schema.getStatus() != null ? transformAnyType(schema.getStatus().chartValues()) : null,
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
            transformAnyType(entity.formSchema()),
            transformAnyType(entity.overlayValues()));

        //
        var status = new TemplateCRDStatus(
            entity.status(), 
            entity.message(),
            transformAnyType(entity.chartSchema()),
            transformAnyType(entity.chartValues()),
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
     *
     */
    private static AnyType transformAnyType(Map<String, Object> map) {
        return map == null ? null : new AnyType(map);
    }
}
