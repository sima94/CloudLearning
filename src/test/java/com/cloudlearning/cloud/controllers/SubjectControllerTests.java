package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.controllers.base.AbstractControllerTests;
import com.cloudlearning.cloud.models.Subject;
import com.cloudlearning.cloud.services.subject.SubjectService;
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

@WebMvcTest(controllers= SubjectController.class, includeFilters = @ComponentScan.Filter(classes= EnableWebSecurity.class))
public class SubjectControllerTests extends AbstractControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SubjectService subjectService;

    @Test
    @WithMockUser(value = "user@test.com")
    public void testGetSubjectReturnStatusOk() throws Exception{

        Subject testSubject = new Subject();
        testSubject.setId(1l);
        testSubject.setName("TestName");
        testSubject.setDescription("TestDesc");

        Mockito.when(subjectService.findById(1L)).thenReturn(testSubject);

        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/subject/1")
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("TestDesc"));
    }

    @Test
    @WithMockUser(value = "user@test.com")
    public void testGetSubjectsReturnStatusOk() throws Exception {

        Subject testSubject1 = new Subject();
        testSubject1.setId(1L);
        testSubject1.setName("TestName1");
        testSubject1.setDescription("TestDesc1");

        Subject testSubject2 = new Subject();
        testSubject2.setId(2L);
        testSubject2.setName("TestName2");
        testSubject2.setDescription("TestDesc2");

        PageRequest pageable = PageRequest.of(0,10);

        PageImpl<Subject> subjectsPage = new PageImpl(List.of(testSubject1, testSubject2),pageable,2L);


        Mockito.when(subjectService.findAll(pageable)).thenReturn(subjectsPage);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/subjects")
                .accept(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", IsCollectionWithSize.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("TestName1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("TestName2"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateSubjectAdminReturnStatusCreated() throws Exception {

        Subject testSubject = new Subject();
        testSubject.setId(1L);
        testSubject.setName("TestName");
        testSubject.setDescription("TestDesc");
        testSubject.setProfessorId(2L);

        Mockito.when(subjectService.create(testSubject)).thenReturn(testSubject);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/subject/create").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(testSubject)));


        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("TestDesc"));
    }

    @Test
    @WithAnonymousUser
    public void testCreateSubjectAnonymousUserReturnStatusForbidden() throws Exception {

        Subject testSubject = new Subject();
        testSubject.setId(1L);
        testSubject.setName("TestName");
        testSubject.setDescription("TestDesc");
        testSubject.setProfessorId(2l);

        Mockito.when(subjectService.create(testSubject)).thenReturn(testSubject);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_ANNOTATIONS);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/subject/create").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(testSubject)));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    public void testConnectStudentWithSubjectWithStudentRoleReturnStatusNoContent() throws Exception {

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/student/subject/1").contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(subjectService, Mockito.times(1)).connectStudentWithSubject(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testConnectStudentWithSubjectWithAdminRoleReturnStatusForbidden() throws Exception {

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/student/subject/1").contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(subjectService, Mockito.times(0)).connectStudentWithSubject(1L);
    }

    @Test
    @WithMockUser(roles = "PROFESSOR")
    public void testConnectStudentWithSubjectWithProfessorRoleReturnStatusForbidden() throws Exception {

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/student/subject/1").contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(subjectService, Mockito.times(0)).connectStudentWithSubject(1L);
    }

    @Test
    @WithMockUser
    public void testGetSubjectsByProfessorIdWithMockUserReturnStatusOk() throws Exception {

        PageRequest pageable = PageRequest.of(0,10);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/professor/1/subjects").contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(subjectService, Mockito.times(1)).findAllForProfessor(1L, pageable);
    }

    @Test
    public void testGetSubjectsByProfessorIdWithOutMockUserReturnStatusForbidden() throws Exception {

        PageRequest pageable = PageRequest.of(0,10);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/professor/1/subjects").contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(subjectService, Mockito.times(0)).findAllForProfessor(1L, pageable);
    }

    @Test
    @WithMockUser
    public void testGetSubjectsByStudentIdWithMockUserReturnStatusOk() throws Exception {

        PageRequest pageable = PageRequest.of(0,10);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/student/1/subjects").contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(subjectService, Mockito.times(1)).findAllForStudent(1L, pageable);
    }

    @Test
    public void testGetSubjectsByStudentIdWithOutMockUserReturnStatusForbidden() throws Exception {

        PageRequest pageable = PageRequest.of(0,10);

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/student/1/subjects").contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(subjectService, Mockito.times(0)).findAllForStudent(1L, pageable);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteSubjectWithAdminReturnStatusNoConnect() throws Exception{

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/subject/1").contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(subjectService, Mockito.times(1)).delete(1L);
    }

    @Test
    @WithAnonymousUser
    public void testDeleteSubjectWithAnonymousUserReturnStatusNoForbidden() throws Exception{

        ResultActions result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/subject/1").contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(subjectService, Mockito.times(0)).delete(1L);
    }

}
