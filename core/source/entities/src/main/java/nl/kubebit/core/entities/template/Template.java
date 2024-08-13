package nl.kubebit.core.entities.template;

import java.util.List;
import java.util.Map;

/**
 * 
 */
public record Template(
    
    String id,
    String chart,    
    String version,
    String repository,

    String type,
    String category,
    String icon,

    TemplateStatus status,
    String message,

    Map<String, Object> schema,
    String appVersion,
    String description,
    List<String> keywords,

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
            status,
            message,
            this.schema(),
            this.appVersion(),
            this.description(),
            this.keywords(),
            this.enviromentId());
    }

    /**
     * 
     * @param chart
     * @return
     */
    public Template setChart(Chart chart) {
        return new Template(
            this.id(),
            this.chart(),
            this.version(),
            this.repository(),
            this.type(),
            this.category(),
            this.icon(),
            this.status(),
            this.message(),
            this.schema(),
            chart != null ? chart.appVersion() : null,
            chart != null ? chart.description() : null,
            chart != null ? chart.keywords() : null,
            this.enviromentId());
    }

    /**
     * 
     * @param schema
     * @return
     */
    public Template setSchema(Map<String, Object> schema) {
        return new Template(
            this.id(),
            this.chart(),
            this.version(),
            this.repository(),
            this.type(),
            this.category(),
            this.icon(),
            this.status(),
            this.message(),
            schema,
            this.appVersion(),
            this.description(),
            this.keywords(),
            this.enviromentId());
    }

    /**
     * 
     * @param type
     * @param category
     * @param icon
     * @return
     */
    public Template setSetting(String type, String category, String icon) {
        return new Template(
            this.id(),
            this.chart(),
            this.version(),
            this.repository(),
            type,
            category,
            icon,
            this.status(),
            this.message(),
            this.schema(),
            this.appVersion(),
            this.description(),
            this.keywords(),
            this.enviromentId());
    }
    
}
