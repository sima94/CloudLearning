package com.cloudlearning.cloud.controllers.security;

import com.cloudlearning.cloud.configuration.oauth2.Oauth2Request;
import com.cloudlearning.cloud.configuration.oauth2.Oauth2Response;
import com.cloudlearning.cloud.controllers.base.AbstractControllerIntegrationTests;
import com.cloudlearning.cloud.models.security.Role;
import com.cloudlearning.cloud.models.security.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class UserControllerIntegrationTest extends AbstractControllerIntegrationTests {

    @Test
    public void testGetUserForId() throws Exception {

        Oauth2Request request = new Oauth2Request("password", "admin@integration.test", "admin@integration");
        Oauth2Response response = oauth2service.getToken(request);

        String token = response.accessToken();
        Long userId = 3L;

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/{id}",userId)
                .header("Authorization", token));

        result.andDo(MockMvcResultHandlers.print()).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("admin@integration.test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role.name").value("ROLE_ADMIN"));
    }

    @Test
    public void testGetLoginUser() throws Exception {

        Oauth2Request request = new Oauth2Request("password", "professor@integration.test", "professor@integration");
        Oauth2Response response = oauth2service.getToken(request);

        String token = response.accessToken();
        Long userId = 2L;

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/me")
                .header("Authorization", token));

        result.andDo(MockMvcResultHandlers.print()).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("professor@integration.test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role.name").value("ROLE_PROFESSOR"));
    }

    @Test
    public void testGetAllUser() throws Exception {

        Oauth2Request request = new Oauth2Request("password", "admin@integration.test", "admin@integration");
        Oauth2Response response = oauth2service.getToken(request);

        String token = response.accessToken();
        Long userId = 2L;

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user")
                .header("Authorization", token));

        result.andDo(MockMvcResultHandlers.print()).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", IsCollectionWithSize.hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].username").value("admin"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].enabled").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].role.name").value("ROLE_ADMIN"));
    }

    @Test
    public void testCreateUser() throws Exception {

        Oauth2Request request = new Oauth2Request("password", "admin@integration.test", "admin@integration");
        Oauth2Response response = oauth2service.getToken(request);

        String token = response.accessToken();

        User user = new User();
        user.setUsername("user@integration.test");
        user.setPassword("user@integration");
        user.setEnabled(true);
        Role adminRole = new Role();
        adminRole.setId(1L);
        user.setRole(adminRole);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user@integration.test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role.id").value(1L));
    }

    @Test
    public void testUpdateUser() throws Exception {

        Oauth2Request request = new Oauth2Request("password", "admin@integration.test", "admin@integration");
        Oauth2Response response = oauth2service.getToken(request);

        String token = response.accessToken();
        Long userId = 5L;

        User user = new User();
        user.setUsername("studentUpdate@integration.test");
        user.setPassword("studentUpdate@integration");
        user.setEnabled(true);
        Role studentRole = new Role();
        studentRole.setId(2L);
        user.setRole(studentRole);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("studentUpdate@integration.test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enabled").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role.name").value("ROLE_STUDENT"));
    }

    //TODO: Test falling for some reason
    @Test
    public void testChangeUserPassword() throws Exception {

        Oauth2Request request = new Oauth2Request("password", "professor@integration.test", "professor@integration");
        Oauth2Response response = oauth2service.getToken(request);

        String token = response.accessToken();

        User user = new User();
        user.setPassword("professor@integration");
        user.setNewPassword("professorNew@integration");
        user.setConfirmNewPassword("professorNew@integration");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user/change/password")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", token)
                .content(objectMapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteUser() throws Exception {

        Oauth2Request request = new Oauth2Request("password", "admin@integration.test", "admin@integration");
        Oauth2Response response = oauth2service.getToken(request);

        String token = response.accessToken();
        Long userId = 4L;

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/user/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token));

        result.andDo(MockMvcResultHandlers.print()).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
