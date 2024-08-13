package nl.kubebit.core.entities.template;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Chart(

    String id,
    String version,
    String appVersion,
    String name,
    String description,
    String type,
    List<String> keywords,
    List<String> urls

) {    
}
