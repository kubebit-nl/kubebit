package nl.kubebit.core.entities.release;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Release entity
 * @param id release id
 * @param version release version
 * @param template template
 * @param values values
 * @param icon icon
 * @param status status
 * @param message message
 * @param resources resources
 * @param revisions revisions
 * @param namespaceId namespace id
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

    String namespaceId

) {

    /**
     * Set status and message
     * @param status status
     * @param message message
     * @return release
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
            this.namespaceId());
    }

    /**
     * Set deployed status and resources
     * @param resources resources
     * @return release
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
            this.namespaceId());
    }

    /**
     * Create a new revision list based on the current release
     * @return the revisions
     */
    public List<ReleaseRef> newRevisions() {
        var ref = new ReleaseRef(this.version(), this.template(), this.values());
        List<ReleaseRef> result = this.revisions() == null ? new ArrayList<>() : new ArrayList<>(this.revisions());
        result.add(ref);

        // trim the list to keep only the last 5 elements
        if (result.size() > 5) {
            result = result.subList(result.size() - 5, result.size());
        }
        return result;
    }

}
