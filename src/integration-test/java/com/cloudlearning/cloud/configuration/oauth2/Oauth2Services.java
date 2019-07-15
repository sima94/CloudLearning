package com.cloudlearning.cloud.configuration.oauth2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Map;

@Service
public class Oauth2Services {

    //private String CLIENT_ID = "spring-security-oauth2-read-write-client";
    private final static String URL = "http://localhost:8080/oauth/token";
    private final static String USERNAME = "spring-security-oauth2-read-write-client";
    private final static String PASSWORD = "spring-security-oauth2-read-write-client-password1234";

    private MockMvc mvc;

    ObjectMapper objectMapper;

    @Autowired
    public Oauth2Services(MockMvc mvc, ObjectMapper objectMapper) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    }

    public Oauth2Response getToken(Oauth2Request oauth2Request) throws Exception {

        Map<String, String> map = objectMapper.convertValue(oauth2Request, new TypeReference<Map<String,String>>() {});

        // Map to MultiValueMap
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        map.entrySet().forEach(e -> parameters.setAll(map));

        ResultActions result
                = mvc.perform(MockMvcRequestBuilders.post(URL)
                .params(oauth2Request.toJson())
                .with(SecurityMockMvcRequestPostProcessors.httpBasic(USERNAME,PASSWORD))
                .accept("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(resultString, Oauth2Response.class);
    }

}
