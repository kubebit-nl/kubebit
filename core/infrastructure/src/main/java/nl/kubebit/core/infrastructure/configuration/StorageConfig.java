package nl.kubebit.core.infrastructure.configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import static nl.kubebit.core.entities.common.vars.GlobalVars.CHARTS_LOCATION;
import static nl.kubebit.core.entities.common.vars.GlobalVars.MANIFESTS_LOCATION;

/**
 * 
 */
@Configuration
public class StorageConfig implements CommandLineRunner {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("init storage locations");

        var path = Paths.get(CHARTS_LOCATION);
        if(!Files.exists(path)) {
            log.debug("creating: " + CHARTS_LOCATION);
            Files.createDirectories(path);
        }

        path = Paths.get(MANIFESTS_LOCATION);
        if(!Files.exists(path)) {
            log.debug("creating: " + MANIFESTS_LOCATION);
            Files.createDirectories(path);
        }
    }

}
