package nl.kubebit.core.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

/**
 * 
 */
@Configuration
public class KubernetesConfig {
   // --------------------------------------------------------------------------------------------
   
    /**
     * 
     * @return
     */
    @Bean
    KubernetesClient kubernetesClient() {
        return new KubernetesClientBuilder().build();
    }

}
