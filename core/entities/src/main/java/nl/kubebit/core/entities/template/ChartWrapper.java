package nl.kubebit.core.entities.template;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChartWrapper(

    String id,
    HashMap<String, List<Chart>> entries

) {
    
}
