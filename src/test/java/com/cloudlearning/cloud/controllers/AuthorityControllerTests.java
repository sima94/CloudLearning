package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.models.security.Authority;
import com.cloudlearning.cloud.services.authority.AuthorityService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers=AuthorityController.class, includeFilters = @ComponentScan.Filter(classes= EnableWebSecurity.class))
@AutoConfigureMockMvc
public class AuthorityControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorityService authorityService;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void whenTryToGetAuthorities_thenFeatureAuthoritiesJson() throws Exception {

        Authority authority1 = new Authority();
        authority1.setId(1L);
        authority1.setName("ADMIN");

        Authority authority2 = new Authority();
        authority2.setId(2L);
        authority2.setName("PROFESSOR");

        Authority authority3 = new Authority();
        authority3.setId(3L);
        authority3.setName("STUDENT");

        PageRequest pageable = PageRequest.of(0,10);

        PageImpl<Authority> authoritiesPage = new PageImpl(List.of(authority1, authority2, authority3),pageable,4L);

        Mockito.when(authorityService.findAll(pageable)).thenReturn(authoritiesPage);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/authority")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", IsCollectionWithSize.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].id").value(3));
    }
}
