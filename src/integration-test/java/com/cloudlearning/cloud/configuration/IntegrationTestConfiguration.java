package com.cloudlearning.cloud.configuration;

import com.cloudlearning.cloud.services.security.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
public class IntegrationTestConfiguration {

//    @Bean
//    @Qualifier("integrationTestUserDetailsService")
//    public UserDetailsService userDetailsService(){
//        return new UserServiceImpl();
//    }
}
