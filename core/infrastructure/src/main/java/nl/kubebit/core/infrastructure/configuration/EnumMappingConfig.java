package nl.kubebit.core.infrastructure.configuration;

import nl.kubebit.core.infrastructure.common.converter.StringToTemplateValueTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nl.kubebit.core.infrastructure.common.converter.StringToTemplateStatusConverter;

/**
 * 
 */
@Configuration
public class EnumMappingConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToTemplateStatusConverter());
        registry.addConverter(new StringToTemplateValueTypeConverter());
    }
    
}
