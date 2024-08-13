package nl.kubebit.core.infrastructure.release.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Plural;
import io.fabric8.kubernetes.model.annotation.Singular;
import io.fabric8.kubernetes.model.annotation.Version;

/**
 * 
 */
@Group("kubebit.nl")
@Version("v1")
@Plural("releases")
@Singular("release")
@Kind("Release")
public class ReleaseCRD extends CustomResource<ReleaseCRDSpec, ReleaseCRDStatus> implements Namespaced {
    // --------------------------------------------------------------------------------------------
    
    //
    @JsonProperty(value = "revision")
    private List<ReleaseCRDSpec> revision;

    //
    public List<ReleaseCRDSpec> getRevision() {
        return revision;
    }

    //
    public void setRevision(List<ReleaseCRDSpec> revision) {
        this.revision = revision;
    }

}
