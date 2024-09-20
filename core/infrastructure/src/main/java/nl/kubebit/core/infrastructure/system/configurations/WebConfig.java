package nl.kubebit.core.infrastructure.system.configurations;

import nl.kubebit.core.infrastructure.system.properties.WebCorsProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 */
@Configuration
@EnableConfigurationProperties({WebCorsProperties.class})
public class WebConfig implements WebMvcConfigurer {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final WebCorsProperties corsProperties;

    /**
     * Constructor
     *
     * @param corsProperties WebCorsProperties
     */
    public WebConfig(WebCorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    /**
     *
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebCorsProperties.Cors cors = corsProperties.cors();
        Objects.requireNonNull(cors, "cors properties are required");
        registry.addMapping("/**")
                .allowedOrigins(cors.allowedOrigins())
                .allowedMethods(cors.allowedMethods())
                .maxAge(cors.maxAge())
                .allowedHeaders(cors.allowedHeaders())
                .exposedHeaders(cors.exposedHeaders());

        log.info("WebConfig.addCorsMappings");
        log.info("cors.allowedOrigins: {}", Arrays.toString(cors.allowedOrigins()));
    }

}
