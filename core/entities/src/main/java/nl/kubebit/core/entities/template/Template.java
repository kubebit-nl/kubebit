package nl.kubebit.core.entities.template;

import java.util.List;
import java.util.Map;

/**
 * 
 */
public record Template(
    
    //
    String id,
    String chart,    
    String version,
    String repository,

    //
    String type,
    String category,
    String icon,

    //
    Map<String, Object> schema,
    String baseValues,
    String stagingValues,
    String productionValues,

    //
    TemplateStatus status,
    String message,
    Map<String, Object> values,
    String appVersion,
    String description,
    List<String> keywords,

    //
    String namespaceId
    
) {
    /**
     *
     */
    public Template setStatus(TemplateStatus status, String message) {
        return new Template(
            this.id(),
            this.chart(),
            this.version(),
            this.repository(),
            this.type(),
            this.category(),
            this.icon(),
            this.schema(),
            this.baseValues(),
            this.stagingValues(),
            this.productionValues(),
            status,
            message,
            this.values(),
            this.appVersion(),
            this.description(),
            this.keywords(),
            this.namespaceId());
    }

}
