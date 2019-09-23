package com.cloudlearning.cloud.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String SECURED_PATTERN = "/api/**";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().requestMatchers()
                .antMatchers(SECURED_PATTERN);
    }


    @Configuration
    protected static class AuthenticationConfiguration extends
            GlobalAuthenticationConfigurerAdapter {

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
            auth.inMemoryAuthentication().withUser("professor").password("professor").roles("PROFESSOR");
            auth.inMemoryAuthentication().withUser("student").password("student").roles("STUDENT");
        }
    }

//    @Bean
//    @Qualifier("testUserDetailsService")
//    public UserDetailsService userDetailsService(){
//        GrantedAuthority authority = new SimpleGrantedAuthority("ADMIN");
//        UserDetails userDetails = new User("admin", "admin", Arrays.asList(authority));
//        return new InMemoryUserDetailsManager(Arrays.asList(userDetails));
//    }
}