package nl.kubebit.core.infrastructure.resource.datasource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 
 */
@SpringBootTest
class ResourceRepositoryTest {
    // --------------------------------------------------------------------------------------------

    @Autowired
    private ResourceRepository repository;

    @Test
    void getResource_when_then() {  
        // given
        // when
        // then
        
        var resource = repository.getResource(
            "7631f6d3-henk", 
            "v1", 
            "Secret", 
            "wordpress");

        System.out.println(resource.get());
    }
}
