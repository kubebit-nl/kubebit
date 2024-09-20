package nl.kubebit.core.usecases.release.chore.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Dynamic properties
 */
@ConfigurationProperties(prefix = "dynamic")
public record DynamicProperties(
        Map<String, Object> properties
) {

}
