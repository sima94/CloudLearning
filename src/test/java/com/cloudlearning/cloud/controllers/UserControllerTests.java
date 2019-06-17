package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.services.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers=UserController.class, includeFilters = @ComponentScan.Filter(classes= EnableWebSecurity.class))
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;


//    @Before
//    public void setup() {
//        mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
//    }

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void whenGetUser_thanGetUserJson() throws Exception {

        User test = new User();
        test.setId(1L);
        test.setUsername("admin@test.com");

        Mockito.when(userService.find(test.getId())).thenReturn(test);

        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/1")
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").isNotEmpty());

    }

    @Test
    @WithMockUser(authorities = {"STUDENT", "PROFESSOR"})
    public void whenTryToGetUser_withPROFESSRforUSEReauthorises_thanReturnForbidden() throws Exception {

        User test = new User();
        test.setId(1L);
        test.setUsername("member@test.com");

        Mockito.when(userService.find(test.getId())).thenReturn(test);

        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/1")
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    public void whenGetCurrentUser_thenGetUserJson() throws Exception{

        User test = new User();
        test.setId(1L);
        test.setUsername("user@test.com");

        Mockito.when(userService.loadUserByUsername(test.getUsername())).thenReturn(test);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/me")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).andExpect(MockMvcResultMatchers.jsonPath("$.username").isNotEmpty());
    }

    @Test
    public void whenGetCurrentUser_andMissingAuthorization_thenReturnUnauthorized() throws Exception{

        User test = new User();
        test.setId(1L);
        test.setUsername("user@test.com");

        Mockito.when(userService.loadUserByUsername(test.getUsername())).thenReturn(test);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/user/me")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
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

}
