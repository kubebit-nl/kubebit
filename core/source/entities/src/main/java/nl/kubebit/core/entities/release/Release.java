package nl.kubebit.core.entities.release;

import java.util.List;
import java.util.Map;

/**
 * 
 */
public record Release(

    String id,

    Long version,
    TemplateRef template,
    Map<String, Object> values,

    String icon,
    ReleaseStatus status,
    String message,
    List<ReleaseResourceRef> resources,

    List<ReleaseRef> revisions,

    String enviromentId

) {
    
    /**
     * 
     * @return
     */
    public Release setStatus(ReleaseStatus status, String message) {
        return new Release(
            this.id(), 
            this.version(), 
            this.template(), 
            this.values(), 
            this.icon(), 
            status,
            message, 
            this.resources(), 
            this.revisions(), 
            this.enviromentId());
    }

    /**
     * 
     * @param status
     * @param message
     * @return
     */
    public Release setDeployedAndResources(List<ReleaseResourceRef> resources) {
        return new Release(
            this.id(), 
            this.version(), 
            this.template(), 
            this.values(), 
            this.icon(), 
            ReleaseStatus.DEPLOYED,
            null, 
            resources, 
            this.revisions(), 
            this.enviromentId());
    }
    
}
