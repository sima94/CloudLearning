package com.cloudlearning.cloud.services;

import com.cloudlearning.cloud.configuration.base.AbstractTests;
import com.cloudlearning.cloud.configuration.utils.authentication.AuthenticationFacade;
import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.Subject;
import com.cloudlearning.cloud.models.members.Professor;
import com.cloudlearning.cloud.models.members.student.Student;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.repositories.SubjectRepository;
import com.cloudlearning.cloud.repositories.members.ProfessorRepository;
import com.cloudlearning.cloud.repositories.members.student.StudentRepository;
import com.cloudlearning.cloud.services.subject.SubjectService;
import com.cloudlearning.cloud.services.subject.SubjectServiceImpl;
import com.sun.tools.javac.util.List;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class SubjectServiceImplTests extends AbstractTests {

    @TestConfiguration
    static class SubjectServiceImplTestContextConfiguration {

        @Bean
        SubjectService subjectService() {
            return new SubjectServiceImpl();
        }
    }

    @Autowired
    private SubjectService subjectService;

    @MockBean
    private SubjectRepository subjectRepositoryMock;

    @MockBean
    private ProfessorRepository professorRepositoryMock;

    @MockBean
    private StudentRepository studentRepositoryMock;

    @MockBean
    private AuthenticationFacade authenticationFacadeMock;

    @Test
    public void testIsSubjectOwnerReturnTrue(){

        Subject testSubject = new Subject();
        testSubject.setId(1L);
        Professor testProfessor = new Professor();
        testSubject.setProfessor(testProfessor);
        testProfessor.setId(2L);
        User testUser = new User();
        testProfessor.setUser(testUser);
        testUser.setUsername("testUsername");

        Mockito.when(subjectRepositoryMock.findById(testSubject.getId())).thenReturn(Optional.of(testSubject));

        assert subjectService.isSubjectOwner(1L,"testUsername");
    }

    @Test
    public void testIsSubjectOwnerReturnFalse(){

        Subject testSubject = new Subject();
        testSubject.setId(1L);
        Professor testProfessor = new Professor();
        testSubject.setProfessor(testProfessor);
        testProfessor.setId(2L);
        User testUser = new User();
        testProfessor.setUser(testUser);
        testUser.setUsername("testUsername");

        Mockito.when(subjectRepositoryMock.findById(testSubject.getId())).thenReturn(Optional.of(testSubject));

        assert !subjectService.isSubjectOwner(1L,"wrongTestUsername");
    }

    @Test
    public void testIsSubjectOwnerTrowsEntityNotExistException(){

        Subject testSubject = new Subject();
        testSubject.setId(1L);

        Mockito.when(subjectRepositoryMock.findById(testSubject.getId())).thenReturn(Optional.empty());

        EntityNotExistException testException = null;
        try {
            subjectService.isSubjectOwner(1L,"wrongTestUsername");
        }catch (EntityNotExistException e){
            testException = e;
        }

        assert testException != null;
    }

    @Test
    public void testFindAllSubject() {

        Subject testSubject1 = new Subject();
        testSubject1.setId(1L);

        Subject testSubject2 = new Subject();
        testSubject2.setId(2L);

        Subject testSubject3 = new Subject();
        testSubject2.setId(3L);

        PageRequest pageable = PageRequest.of(1,10);

        PageImpl<Subject> mockSubjectsPage = new PageImpl<>(List.of(testSubject1,testSubject2, testSubject3),pageable,3L);

        Mockito.when(subjectRepositoryMock.findAll(pageable)).thenReturn(mockSubjectsPage);

        Page<Subject> subjectsPage = subjectService.findAll(pageable);


        assert subjectsPage.getContent().size() == 3;
        assert subjectsPage.getContent().get(0) == testSubject1;
        assert subjectsPage.getContent().get(2) == testSubject3;
    }

    @Test
    public void testFindAllSubjectForProfessor() {

        Subject testSubject1 = new Subject();
        testSubject1.setId(1L);

        Subject testSubject2 = new Subject();
        testSubject2.setId(2L);

        Subject testSubject3 = new Subject();
        testSubject2.setId(3L);

        PageRequest pageable = PageRequest.of(1,10);

        PageImpl<Subject> mockSubjectsPage = new PageImpl<>(List.of(testSubject1,testSubject2, testSubject3),pageable,3L);

        Mockito.when(subjectRepositoryMock.findSubjectsByProfessorId(1L,pageable)).thenReturn(mockSubjectsPage);

        Page<Subject> subjectsPage = subjectService.findAllForProfessor(1L, pageable);

        assert subjectsPage.getContent().size() == 3;
        assert subjectsPage.getContent().get(0) == testSubject1;
        assert subjectsPage.getContent().get(2) == testSubject3;
    }

    @Test
    public void testFindAllSubjectForStudent() {

        Subject testSubject1 = new Subject();
        testSubject1.setId(1L);

        Subject testSubject2 = new Subject();
        testSubject2.setId(2L);

        Subject testSubject3 = new Subject();
        testSubject2.setId(3L);

        PageRequest pageable = PageRequest.of(1,10);

        PageImpl<Subject> mockSubjectsPage = new PageImpl<>(List.of(testSubject1,testSubject2, testSubject3),pageable,3L);

        Mockito.when(subjectRepositoryMock.findSubjectsByStudentsId(1L,pageable)).thenReturn(mockSubjectsPage);

        Page<Subject> subjectsPage = subjectService.findAllForStudent(1L, pageable);

        assert subjectsPage.getContent().size() == 3;
        assert subjectsPage.getContent().get(0) == testSubject1;
        assert subjectsPage.getContent().get(2) == testSubject3;
    }

    @Test
    public void testCreateSubject() {

        Subject testSubject1 = new Subject();
        testSubject1.setId(1L);
        testSubject1.setProfessorId(1L);

        Professor testProfessor1 = new Professor();
        testProfessor1.setId(1L);

        Mockito.when(professorRepositoryMock.findById(testProfessor1.getId())).thenReturn(Optional.of(testProfessor1));
        Mockito.when(subjectRepositoryMock.save(testSubject1)).thenReturn(testSubject1);

        Subject createdSubject = subjectService.create(testSubject1);

        Mockito.verify(subjectRepositoryMock, Mockito.times(1)).save(testSubject1);
        assert createdSubject == testSubject1;
        assert createdSubject.getProfessor() == testProfessor1;
    }

    @Test
    public void testCreateSubjectThrowEntityNotExistException() {

        Subject testSubject1 = new Subject();
        testSubject1.setId(1L);
        testSubject1.setProfessorId(1L);

        Professor testProfessor1 = new Professor();
        testProfessor1.setId(1L);

        Mockito.when(professorRepositoryMock.findById(testProfessor1.getId())).thenReturn(Optional.empty());

        Subject createdSubject = null;

        EntityNotExistException testException = null;
        try {
             createdSubject = subjectService.create(testSubject1);
        }catch (EntityNotExistException e){
            testException = e;
        }

        Mockito.verify(subjectRepositoryMock, Mockito.times(0)).save(testSubject1);
        assert testException != null;
    }

    @Test
    public void testConnectStudentWithSubject() {

        Subject testSubject1 = new Subject();
        testSubject1.setId(1L);
        testSubject1.setStudents(new HashSet<>());

        Student testStudent1 = new Student();
        testStudent1.setId(1L);

        Authentication authentication = getTestAuthentication("testUsername");

        Mockito.when(authenticationFacadeMock.getAuthentication()).thenReturn(authentication);

        Mockito.when(studentRepositoryMock.findStudentByUserUsername("testUsername")).thenReturn(Optional.of(testStudent1));
        Mockito.when(subjectRepositoryMock.findById(testSubject1.getId())).thenReturn(Optional.of(testSubject1));
        Mockito.when(subjectRepositoryMock.save(testSubject1)).thenReturn(testSubject1);

        subjectService.connectStudentWithSubject(testSubject1.getId());

        Mockito.verify(subjectRepositoryMock, Mockito.times(1)).save(testSubject1);
        assert testSubject1.getStudents().contains(testStudent1);
    }

    @Test
    public void testConnectStudentWithSubjectThrowStudentEntityNotExistException() {

        Subject testSubject = new Subject();
        testSubject.setId(1L);
        testSubject.setStudents(new HashSet<>());

        Student testStudent = new Student();
        testStudent.setId(1L);

        Authentication authentication = getTestAuthentication("testUsername");

        Mockito.when(authenticationFacadeMock.getAuthentication()).thenReturn(authentication);

        Mockito.when(studentRepositoryMock.findStudentByUserUsername("testUsername")).thenReturn(Optional.empty());
        Mockito.when(subjectRepositoryMock.findById(testSubject.getId())).thenReturn(Optional.of(testSubject));


        EntityNotExistException testException = null;
        try {
            subjectService.connectStudentWithSubject(testSubject.getId());
        }catch (EntityNotExistException e){
            testException = e;
        }

        assert testException != null;
        Mockito.verify(subjectRepositoryMock, Mockito.times(0)).save(testSubject);

    }

    @Test
    public void testConnectStudentWithSubjectThrowSubjectEntityNotExistException() {

        Subject testSubject1 = new Subject();
        testSubject1.setId(1L);
        testSubject1.setStudents(new HashSet<>());

        Student testStudent1 = new Student();
        testStudent1.setId(1L);

        Authentication authentication = getTestAuthentication("testUsername");

        Mockito.when(authenticationFacadeMock.getAuthentication()).thenReturn(authentication);

        Mockito.when(studentRepositoryMock.findStudentByUserUsername("testUsername")).thenReturn(Optional.empty());
        Mockito.when(subjectRepositoryMock.findById(testSubject1.getId())).thenReturn(Optional.of(testSubject1));

        EntityNotExistException testException = null;
        try {
            subjectService.connectStudentWithSubject(testSubject1.getId());
        }catch (EntityNotExistException e){
            testException = e;
        }

        assert testException != null;
        Mockito.verify(subjectRepositoryMock, Mockito.times(0)).save(testSubject1);

    }

    @Test
    public void testDeleteSubject() {

        Subject testSubject = new Subject();
        testSubject.setId(1L);

        subjectService.delete(1L);

        Mockito.verify(subjectRepositoryMock, Mockito.times(1)).deleteById(testSubject.getId());
    }

    @Test
    public void testDeleteSubjectThrowEntityNotExistException() {
        Subject testSubject = new Subject();
        testSubject.setId(1L);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(subjectRepositoryMock).deleteById(testSubject.getId());

        EntityNotExistException testException = null;
        try {
            subjectService.delete(1L);
        }catch (EntityNotExistException e){
            testException = e;
        }

        assert testException != null;
        Mockito.verify(subjectRepositoryMock, Mockito.times(1)).deleteById(testSubject.getId());
    }

    private Authentication getTestAuthentication(String username) {
        return new Authentication(){
            @Override
            public String getName() {
                return username;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
        };
    }

}
