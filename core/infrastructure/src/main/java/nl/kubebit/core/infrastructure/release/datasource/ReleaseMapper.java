package nl.kubebit.core.infrastructure.release.datasource;

import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.AnyType;
import io.fabric8.kubernetes.api.model.ObjectMeta;

import nl.kubebit.core.entities.release.Release;
import nl.kubebit.core.entities.release.ReleaseRef;
import nl.kubebit.core.entities.release.ReleaseStatus;

import nl.kubebit.core.infrastructure.release.schema.ReleaseCRD;
import nl.kubebit.core.infrastructure.release.schema.ReleaseCRDSpec;
import nl.kubebit.core.infrastructure.release.schema.ReleaseCRDStatus;

/**
 * 
 */
public abstract class ReleaseMapper {
    // --------------------------------------------------------------------------------------------

    /**
     *
     */
    public static Release toEntity(ReleaseCRD schema) {
        return new Release(
            schema.getMetadata().getName(),
            schema.getSpec().version(),
            schema.getSpec().template(),
            transformAnyType(schema.getSpec().values()),
            schema.getStatus() != null ? schema.getStatus().icon() : null,
            schema.getStatus() != null ? schema.getStatus().status() : ReleaseStatus.UNKNOWN,
            schema.getStatus() != null ? schema.getStatus().message() : null,
            schema.getStatus() != null ? schema.getStatus().resources() : null,
            schema.getRevision() != null ? schema.getRevision().stream().map(ReleaseMapper::toEntity).toList() : List.of(),
            schema.getMetadata().getNamespace());
    }

    /**
     *
     */
    public static ReleaseCRD toSchema(Release entity) {

        //
        var meta = new ObjectMeta();
        meta.setName(entity.id());
        meta.setNamespace(entity.namespaceId());

        //
        var spec = new ReleaseCRDSpec(
            entity.version(),
            entity.template(),
            transformAnyType(entity.values()));

        //
        var status = new ReleaseCRDStatus(
            entity.icon(),
            entity.status(),
            entity.message(),
            entity.resources()
        );

        //
        var schema = new ReleaseCRD();
        schema.setMetadata(meta);
        schema.setSpec(spec);
        schema.setStatus(status);
        schema.setRevision(entity.revisions().stream().map(ReleaseMapper::toSchema).toList());

        //
        return schema;
    }

    // ----------------------------------

    /**
     *
     */
    public static ReleaseRef toEntity(ReleaseCRDSpec schema) {
        return new ReleaseRef(schema.version(), schema.template(), transformAnyType(schema.values()));
    }

    /**
     *
     */
    private static ReleaseCRDSpec toSchema(ReleaseRef entity) {
        return new ReleaseCRDSpec(entity.version(), entity.template(), transformAnyType(entity.values()));
    }

    // ----------------------------------

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
