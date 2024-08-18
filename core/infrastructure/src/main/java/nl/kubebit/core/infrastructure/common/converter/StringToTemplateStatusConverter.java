package nl.kubebit.core.infrastructure.common.converter;

import org.springframework.core.convert.converter.Converter;

import nl.kubebit.core.entities.template.TemplateStatus;

/**
 * 
 */
public class StringToTemplateStatusConverter implements Converter<String, TemplateStatus> {

    /**
     * 
     */
    @Override
    public TemplateStatus convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return TemplateStatus.valueOf(source.toUpperCase());
    }
    
}
