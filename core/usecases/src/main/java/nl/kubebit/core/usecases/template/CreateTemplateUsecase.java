package nl.kubebit.core.usecases.template;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
public interface CreateTemplateUsecase {
    
    //
    TemplateResponse execute(TemplateCreateRequest request);

    /**
     * 
     */
    public record TemplateCreateRequest(

        @NotBlank
        @Pattern(regexp = "[a-z0-9]([-a-z0-9]*[a-z0-9])?")
        String chart,

        @NotBlank
        @Pattern(regexp = "^[0-9]+\\.[0-9]+\\.[0-9]+$")
        String version,

        @URL
        @NotBlank
        @Pattern(regexp = "^https.*$")
        String repository

    ) {

    }

}
