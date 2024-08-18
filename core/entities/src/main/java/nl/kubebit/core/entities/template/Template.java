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
    Map<String, Object> formSchema,
    Map<String, Object> overlayValues,

    //
    TemplateStatus status,
    String message,

    Map<String, Object> chartSchema,
    Map<String, Object> chartValues,
    String appVersion,
    String description,
    List<String> keywords,

    //
    String enviromentId
    
) {
    /**
     * 
     * @param status
     * @param message
     * @return
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
            this.formSchema(),
            this.overlayValues(),
            status,
            message,
            this.chartSchema(),
            this.chartValues(),
            this.appVersion(),
            this.description(),
            this.keywords(),
            this.enviromentId());
    }

}
