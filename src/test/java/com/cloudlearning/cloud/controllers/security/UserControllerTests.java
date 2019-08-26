package com.cloudlearning.cloud.controllers.security;

import com.cloudlearning.cloud.controllers.base.AbstractControllerTests;
import com.cloudlearning.cloud.global.exception.entity.EntityAlreadyExistExeption;
import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.security.Role;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.services.security.user.UserService;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.util.List;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers= UserController.class, includeFilters = @ComponentScan.Filter(classes= EnableWebSecurity.class))
public class UserControllerTests extends AbstractControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
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
    @WithMockUser(roles = {"ADMIN"})
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
    public void whenTryToCreateUser_thanReturnStatus200AndNewUser() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Role adminRole = new Role();
        adminRole.setId(1L);
        user.setRole(adminRole);

        Mockito.when(userService.create(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user@test.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
        Role adminRole = new Role();
        adminRole.setId(1L);
        user.setRole(adminRole);

        Mockito.when(userService.create(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void whenTryToCreateUser_withExistingUsername_thanReturnExpectationFail() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Role adminRole = new Role();
        adminRole.setId(1L);
        user.setRole(adminRole);

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
    @WithMockUser(roles = "ADMIN")
    public void whenTryToCreateUser_withWrongAuthorityId_thanReturnNotFound() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Role adminRole = new Role();
        adminRole.setId(1L);
        user.setRole(adminRole);

        String authorityEntityNotExist = "authorityEntityNotExist";

        Mockito.doThrow(new EntityNotExistException(authorityEntityNotExist)).when(userService).create(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(authorityEntityNotExist));
    }

    @Test
    @WithMockUser(value = "user@test.com", roles = "ADMIN")
    public void whenTryToUpdateNewUser_thenReturnNewUser() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Role adminRole = new Role();
        adminRole.setId(1L);
        user.setRole(adminRole);

        Mockito.when(userService.update(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/user/{id}", user.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user@test.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role.id").value(1L));
    }

    @Test
    public void whenTryToUpdateNewUser_thenReturnForbidden() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Role adminRole = new Role();
        adminRole.setId(1L);
        user.setRole(adminRole);

        Mockito.when(userService.update(user)).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/user/{id}", user.getId()).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(value = "user@test.com", roles = "ADMIN")
    public void whenTryToUpdateNewUser_andUserNotExist_thenReturnNotFound() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("user@test.com");
        user.setPassword("userPassword");
        user.setEnabled(true);
        Role adminRole = new Role();
        adminRole.setId(1L);
        user.setRole(adminRole);

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
    @WithMockUser(roles = "ADMIN")
    public void whenTryToDeleteUser_thenReturnNoContent() throws Exception {

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/user/{id}",1));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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

