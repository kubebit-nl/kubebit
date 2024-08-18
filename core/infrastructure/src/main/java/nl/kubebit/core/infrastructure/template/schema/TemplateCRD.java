package nl.kubebit.core.infrastructure.template.schema;

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
@Plural("templates")
@Singular("template")
@Kind("Template")
public class TemplateCRD extends CustomResource<TemplateCRDSpec, TemplateCRDStatus> implements Namespaced {
    // --------------------------------------------------------------------------------------------
    
}
