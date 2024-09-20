package nl.kubebit.core.usecases.template.admin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

/**
 * 
 */
public interface CreateTemplateUseCase {
    
    //
    TemplateItemResponse execute(

            @NotNull
            @Valid
            TemplateCreateRequest request);

    /**
     * 
     */
    @JsonPropertyOrder({"chart", "version", "repository"})
    record TemplateCreateRequest(

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
