package nl.kubebit.core.usecases.template.admin;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

/**
 * 
 */
public interface UpdateTemplateValuesUseCase {
    
    TemplateItemResponse execute(

            @NotBlank
            String templateId,

            @NotNull
            TemplateValueType type,

            @NotNull
            Map<String, Object> overlay

    ) throws TemplateNotFoundException;


    /**
     *
     */
    enum TemplateValueType {
        @JsonProperty("base")
        BASE,
        @JsonProperty("staging")
        STAGING,
        @JsonProperty("production")
        PRODUCTION
    }

}
