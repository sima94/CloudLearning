package com.cloudlearning.cloud.configuration.base;

import com.cloudlearning.cloud.CloudApplication;
import com.cloudlearning.cloud.configuration.IntegrationTestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudApplication.class)
@TestPropertySource(locations = "classpath:integration-test/application-integration-test.properties")
@Import(IntegrationTestConfiguration.class)
public abstract class AbstractIntegrationTests {
}
