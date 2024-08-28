package nl.kubebit.core.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.LoaderOptions;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * 
 */
@Configuration
public class YamlConfig {
    // --------------------------------------------------------------------------------------------

    /**
     * enable YAMLFactory with custom options
     */
    @Bean
    YAMLFactory initYAMLFactory() {
        var options = new LoaderOptions();
        options.setCodePointLimit(100 * 1024 * 1024); //100MB
        return YAMLFactory.builder().loaderOptions(options).build();
    }

}
