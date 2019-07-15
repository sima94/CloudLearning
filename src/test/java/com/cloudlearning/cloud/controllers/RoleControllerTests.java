package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.controllers.base.AbstractControllerTests;
import com.cloudlearning.cloud.models.security.Role;
import com.cloudlearning.cloud.services.role.RoleService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers= RoleController.class, includeFilters = @ComponentScan.Filter(classes= EnableWebSecurity.class))
public class RoleControllerTests extends AbstractControllerTests{

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoleService roleService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void whenTryToGetAuthorities_thenFeatureAuthoritiesJson() throws Exception {

        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("PROFESSOR");

        Role role3 = new Role();
        role3.setId(3L);
        role3.setName("STUDENT");

        PageRequest pageable = PageRequest.of(0,10);

        PageImpl<Role> authoritiesPage = new PageImpl(List.of(role1, role2, role3),pageable,4L);

        Mockito.when(roleService.findAll(pageable)).thenReturn(authoritiesPage);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/role")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", IsCollectionWithSize.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].id").value(3));
    }
}
