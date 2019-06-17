package com.cloudlearning.cloud.configuration;

import com.cloudlearning.cloud.configuration.encryption.Encoders;
import com.cloudlearning.cloud.models.security.Authority;
import com.cloudlearning.cloud.models.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

//@TestConfiguration
//public class SpringSecurityWebAuxTestConfig {
//
//    @Autowired
//    Encoders encoders;
//
//    @Bean
//    @Primary
//    public UserDetailsService userDetailsService() {
//        User admin = new User();
//        admin.setUsername("admin@test.com");
//        admin.setPassword(encoders.userPasswordEncoder().encode("admin"));
//        admin.setAuthority(new Authority(1L,"ADMIN"));
//        admin.setEnabled(true);
//        admin.setCredentialsExpired(false);
//        admin.setAccountLocked(false);
//
//        User professor = new User();
//        professor.setUsername("professor@test.com");
//        professor.setPassword(encoders.userPasswordEncoder().encode("professor"));
//        professor.setAuthority(new Authority(2L,"PROFESSOR"));
//        professor.setEnabled(true);
//        professor.setCredentialsExpired(false);
//        professor.setAccountLocked(false);
//
//        User student = new User();
//        student.setUsername("student@test.com");
//        student.setPassword("student");
//        student.setAuthority(new Authority(3L,"STUDENT"));
//        student.setEnabled(true);
//        student.setCredentialsExpired(false);
//        student.setAccountLocked(false);
//
//
//        return new InMemoryUserDetailsManager(Arrays.asList(
//                admin, professor, student
//        ));
//    }
//}
