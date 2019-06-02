package com.cloudlearning.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = JmxAutoConfiguration.class)
public class CloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }

}
