package nl.kubebit.core.entities.release;

import com.fasterxml.jackson.annotation.JsonProperty;

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
     *
     */
    public String id() {
        return chart + "-" + version;
    }
    
}
