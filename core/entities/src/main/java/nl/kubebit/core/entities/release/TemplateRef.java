package nl.kubebit.core.entities.release;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.kubebit.core.entities.template.Template;

/**
 *
 */
public record TemplateRef(

        @JsonProperty("chart")
        String chart,

        @JsonProperty("version")
        String version

) {

    /**
     * Constructor
     *
     * @param template the template
     */
    public TemplateRef(Template template) {
        this(
                template.chart(),
                template.version());
    }


    /**
     * Getting the id
     */
    public String id() {
        return chart + "-" + version;
    }

}
