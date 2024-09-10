package nl.kubebit.core.infrastructure.common.converter;

import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.usecases.template.admin.UpdateTemplateValuesUseCase;
import org.springframework.core.convert.converter.Converter;

/**
 * 
 */
public class StringToTemplateValueTypeConverter implements Converter<String, UpdateTemplateValuesUseCase.TemplateValueType> {

    /**
     * 
     */
    @Override
    public UpdateTemplateValuesUseCase.TemplateValueType convert(String source) {
        if (source.isEmpty()) {
            return null;
        }
        return UpdateTemplateValuesUseCase.TemplateValueType.valueOf(source.toUpperCase());
    }
    
}
