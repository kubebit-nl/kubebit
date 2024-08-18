package nl.kubebit.core.entities.template.validator;

import java.util.Arrays;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import nl.kubebit.core.entities.template.TemplateStatus;

/**
 * 
 */
public class HasTemplateStatusValidator implements ConstraintValidator<HasTemplateStatus, TemplateStatus> {

    //
    private TemplateStatus[] subset;

    //
    @Override
    public void initialize(HasTemplateStatus constraint) {
        this.subset = constraint.anyOf();
    }

    /**
     * 
     */
    @Override
    public boolean isValid(TemplateStatus value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }

}