package nl.kubebit.core.infrastructure.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 
 */
@Profile(value = { "!nocache", "!test" })
@EnableCaching
@Configuration
public class CacheConfig {
    // --------------------------------------------------------------------------------------------

    //
    public static final String CACHE_PROJECTS = "projects";
    public static final String CACHE_PROJECT = "project";
    
}
