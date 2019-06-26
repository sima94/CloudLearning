package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.exeptions.entity.EntityAlreadyExistExeption;
import com.cloudlearning.cloud.exeptions.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.security.Authority;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.services.user.UserService;
import com.cloudlearning.cloud.services.user.exceptions.AuthorityEntityNotExistException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.util.List;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers=UserController.class, includeFilters = @ComponentScan.Filter(classes= EnableWebSecurity.class))
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void whenGetUser_thanGetUserJson() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("admin@test.com");

        Mockito.when(userService.find(user.getId())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/1")
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").isNotEmpty());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void whenTryToGetUserWithWrongId_thanReturn404() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("admin@test.com");

        String errorMessage = "notFound";

        Mockito.doThrow( new EntityNotExistException(errorMessage)).when(userService).find(user.getId());

        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/1")
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));
    }

    @Test
    @WithAnonymousUser
    public void whenTryToGetUser_withPROFESSRforUSEReauthorises_thanReturnForbidden() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("member@test.com");


        Mockito.when(userService.find(user.getId())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/1")
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    public void whenGetCurrentUser_thenGetUserJson() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");

        Mockito.when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/me")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").isNotEmpty());
    }

    @Test
    public void whenGetCurrentUser_andMissingAuthorization_thenReturnUnauthorized() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");

        Mockito.when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/me")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void whenGetUsers_thenReturnUsersJson() throws Exception {

        User testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("user1@test.com");

        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("user2@test.com");

        PageRequest pageable = PageRequest.of(0,10);

        PageImpl<User> usersPage = new PageImpl(List.of(testUser1, testUser2),pageable,3L);

        Mockito.when(userService.findAll(pageable)).thenReturn(usersPage);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content", IsCollectionWithSize.hasSize(2)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].username").value("user1@test.com"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].username").value("user2@test.com"));
    }

    @Test
    @WithAnonymousUser
    public void whenGetUsers_thenReturnForbidden() throws Exception {

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void whenGetUsers_thenReturnUnauthorized() throws Exception {

        PageRequest pageable = PageRequest.of(0,10);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void whenTryToCreateUser_thanReturnStatus200AndNewUser() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Authority adminAuthority = new Authority();
        adminAuthority.setId(1L);
        user.setAuthority(adminAuthority);

        Mockito.when(userService.create(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user@test.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authority.id").value(1L));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void whenTryToCreateUser_withWrongParams_thanReturnStatus422AndValidationErrors() throws Exception {

        User user = new User();

        Mockito.when(userService.create(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(422))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("api.error.validation"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors", IsCollectionWithSize.hasSize(3)));
    }

    @Test
    @WithAnonymousUser
    public void whenTryToCreateUser_thanReturnStatusForbidden() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Authority adminAuthority = new Authority();
        adminAuthority.setId(1L);
        user.setAuthority(adminAuthority);

        Mockito.when(userService.create(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void whenTryToCreateUser_withExistingUsername_thanReturnExpectationFail() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Authority adminAuthority = new Authority();
        adminAuthority.setId(1L);
        user.setAuthority(adminAuthority);

        String usernameAlreadyExist = "usernameExist";

        Mockito.doThrow(new EntityAlreadyExistExeption(usernameAlreadyExist)).when(userService).create(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isExpectationFailed())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(417))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(usernameAlreadyExist));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void whenTryToCreateUser_withWrongAuthorityId_thanReturnNotFound() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Authority adminAuthority = new Authority();
        adminAuthority.setId(1L);
        user.setAuthority(adminAuthority);

        String authorityEntityNotExist = "authorityEntityNotExist";

        Mockito.doThrow(new AuthorityEntityNotExistException(authorityEntityNotExist)).when(userService).create(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(authorityEntityNotExist));
    }

    @Test
    @WithMockUser(value = "user@test.com")
    public void whenTryToUpdateNewUser_thenReturnNewUser() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Authority adminAuthority = new Authority();
        adminAuthority.setId(1L);
        user.setAuthority(adminAuthority);

        Mockito.when(userService.update(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/user/{id}", user.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user@test.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authority.id").value(1L));
    }

    @Test
    public void whenTryToUpdateNewUser_thenReturnForbidden() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Authority adminAuthority = new Authority();
        adminAuthority.setId(1L);
        user.setAuthority(adminAuthority);

        Mockito.when(userService.update(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/user/{id}", user.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(value = "user@test.com")
    public void whenTryToUpdateNewUser_andUserNotExist_thenReturnNotFound() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Authority adminAuthority = new Authority();
        adminAuthority.setId(1L);
        user.setAuthority(adminAuthority);

        String errorMessage = "notFound";

        Mockito.doThrow(new EntityNotExistException(errorMessage)).when(userService).update(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/user/{id}", user.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));
    }

    @Test
    public void whenTryToUpdateUser_thenReturnUnprocessableEntity() throws Exception {

        User user = new User();
        user.setId(1L);

        Mockito.when(userService.update(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/user/{id}", user.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(422))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("api.error.validation"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors", IsCollectionWithSize.hasSize(1)));
    }

    @Test
    @WithMockUser(value = "admin")
    public void whenTryToChangeUserPassword_thenReturnReturnStatusNoContent() throws Exception {

        User user = new User();
        user.setPassword("oldPassword");
        user.setNewPassword("newPassword");
        user.setConfirmNewPassword("newPassword");

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user/change/password").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(value = "admin")
    public void whenTryToChangeUserPassword_witMissingData_thenReturnReturnUnprocessableEntity() throws Exception {

        User user = new User();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user/change/password").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(422))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("api.error.validation"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validationErrors", IsCollectionWithSize.hasSize(3)));
    }

    @Test
    public void whenTryToChangeUserPassword_withoutAccessToken_thenReturnForbidden() throws Exception {

        User user = new User();
        user.setPassword("oldPassword");
        user.setNewPassword("newPassword");
        user.setConfirmNewPassword("newPassword");

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user/change/password").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
        Mockito.verify(userService, Mockito.times(0)).changePassword(user);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void whenTryToDeleteUser_thenReturnNoContent() throws Exception {

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/user/{id}",1));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void whenTryToDeleteUser_andUserNotExist_thenReturnNotFound() throws Exception {

        Long userId = 1L;

        String errorMessage = "notFound";

        Mockito.doThrow( new EntityNotExistException(errorMessage)).when(userService).delete(userId);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/user/{id}",userId));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorMessage));

    }

    @Test
    public void whenTryToDeleteUser_thenReturnForbidden() throws Exception {

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/user/{id}",1));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}

//    private String obtainAccessToken(String username, String password) throws Exception {
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "password");
//        params.add("client_id", "spring-security-oauth2-read-write-client");
//        params.add("username", username);
//        params.add("password", password);
//
//        ResultActions result
//                = mvc.perform(MockMvcRequestBuilders.post("/oauth/token")
//                .params(params)
//                .with(SecurityMockMvcRequestPostProcessors.httpBasic("spring-security-oauth2-read-write-client","spring-security-oauth2-read-write-client-password1234"))
//                .accept("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"));
//
//        String resultString = result.andReturn().getResponse().getContentAsString();
//
//        JacksonJsonParser jsonParser = new JacksonJsonParser();
//        return jsonParser.parseMap(resultString).get("access_token").toString();
//    }