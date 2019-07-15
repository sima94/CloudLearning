package com.cloudlearning.cloud.configuration.base;

import com.cloudlearning.cloud.CloudApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudApplication.class)
@TestPropertySource(locations = "classpath:integration-test/application-integration-test.properties")
public abstract class AbstractIntegrationTests {
}
