package nl.kubebit.core.infrastructure;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 
 */
@ActiveProfiles("smoke")
@SpringBootTest(classes = Application.class)
class ApplicationTests {

	//
	@Test
	void smokeTest() {
		// running spring boot application is the test
	}

}
