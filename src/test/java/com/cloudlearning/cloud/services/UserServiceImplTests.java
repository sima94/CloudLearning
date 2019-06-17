package com.cloudlearning.cloud.services;

import com.cloudlearning.cloud.configuration.encryption.Encoders;
import com.cloudlearning.cloud.exeptions.entity.EntityAlreadyExistExeption;
import com.cloudlearning.cloud.exeptions.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.security.Authority;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.repositories.AuthorityRepository;
import com.cloudlearning.cloud.repositories.UserRepository;
import com.cloudlearning.cloud.services.user.UserService;
import com.cloudlearning.cloud.services.user.UserServiceImpl;
import com.sun.tools.javac.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Collections;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class UserServiceImplTests {

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private AuthorityRepository authorityRepositoryMock;

    @MockBean
    private Encoders encodersMock;

    @MockBean
    private UserRepository userRepositoryMock;

    @Test
    public void whenValidUsername_thanUserShouldBeFound() {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("usern@test.com");

        Mockito.when(userRepositoryMock.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        User user = (User)userService.loadUserByUsername(testUser.getUsername());

        assert user.getId().equals(testUser.getId());
        assert user.getUsername().equals(testUser.getUsername());
    }

    @Test
    public void whenUsernameNotExist_thanThrowUsernameNotFoundException() {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user@test.com");

        Mockito.when(userRepositoryMock.findByUsername(testUser.getUsername())).thenReturn(Optional.ofNullable(null));

        UsernameNotFoundException usernameNotFoundException = null;
        try {
            userService.loadUserByUsername(testUser.getUsername());
        } catch (UsernameNotFoundException e) {
            usernameNotFoundException = e;
        }


        assert usernameNotFoundException != null;
    }

    @Test
    public void whenValidUserId_thenUserShouldBeFound() {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user@test.com");

        Mockito.when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        User user = userService.find(testUser.getId());

        assert user.getId().equals(testUser.getId());
        assert user.getUsername().equals(testUser.getUsername());
    }

    @Test
    public void whenTryToCreateValidNewUser_thenShouldReturnNewUser(){

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user@test.com");
        testUser.setPassword("testPassword");

        Authority testAuthority = new Authority();
        testAuthority.setId(1L);

        testUser.setAuthority(testAuthority);

        PasswordEncoder userPasswordEncoder = new Encoders().userPasswordEncoder();

        Mockito.when(userRepositoryMock.findByUsernameOrId(testUser.getUsername(),testUser.getId())).thenReturn(Collections.emptyList());
        Mockito.when(authorityRepositoryMock.findById(testAuthority.getId())).thenReturn(Optional.of(testAuthority));
        Mockito.when(encodersMock.userPasswordEncoder()).thenReturn(userPasswordEncoder);

        Mockito.when(userRepositoryMock.save(testUser)).thenReturn(testUser);

        User createdUser = userService.create(testUser);

        assert createdUser.getId().equals(testUser.getId());
        assert createdUser.getUsername().equals(testUser.getUsername());
        assert userPasswordEncoder.matches("testPassword", createdUser.getPassword());
        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(testUser);
    }

    @Test
    public void whenTryToCreateNewUserWithExistingUsername_thenShouldThrowEntityAlreadyExistException(){

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user@test.com");
        testUser.setPassword("testPassword");

        Mockito.when(userRepositoryMock.findByUsernameOrId(testUser.getUsername(),testUser.getId())).thenReturn(List.of(testUser));

        EntityAlreadyExistExeption testException = null;
        try {
            userService.create(testUser);
        }catch (EntityAlreadyExistExeption e){
            testException = e;
        }

        assert testException != null;
        assert testException.getMessage().equals("api.error.user.usernameAlreadyExist");
        Mockito.verify(userRepositoryMock, Mockito.times(0)).save(testUser);
    }

    @Test
    public void whenTryToCreateNewUserWithNotExistingAuthority_thenShouldThrowEntityNotExistException(){

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user@test.com");
        testUser.setPassword("testPassword");

        Authority testAuthority = new Authority();
        testAuthority.setId(999L);

        testUser.setAuthority(testAuthority);

        PasswordEncoder userPasswordEncoder = new Encoders().userPasswordEncoder();

        Mockito.when(userRepositoryMock.findByUsernameOrId(testUser.getUsername(),testUser.getId())).thenReturn(Collections.emptyList());
        Mockito.when(authorityRepositoryMock.findById(testAuthority.getId())).thenReturn(Optional.ofNullable(null));
        Mockito.when(encodersMock.userPasswordEncoder()).thenReturn(userPasswordEncoder);

        Mockito.when(userRepositoryMock.save(testUser)).thenReturn(testUser);

        EntityNotExistException testException = null;
        try {
            userService.create(testUser);
        }catch (EntityNotExistException e){
            testException = e;
        }

        assert testException != null;
        assert testException.getMessage().equals("api.error.authority.notExist");
        Mockito.verify(userRepositoryMock, Mockito.times(0)).save(testUser);
    }

    @Test
    public void whenTryToUpdateUser_thenShouldReturnOldUpdatedUser(){

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user@test.com");
        testUser.setPassword("testPassword");
        testUser.setAccountExpired(false);
        testUser.setAccountLocked(false);
        testUser.setCredentialsExpired(false);
        testUser.setEnabled(true);
        Authority newAuthority = new Authority();
        newAuthority.setId(99L);
        testUser.setAuthority(newAuthority);

        User oldUser = new User();
        oldUser.setId(1L);
        oldUser.setUsername("oldUser@test.com");
        oldUser.setPassword("toldTestPassword");
        oldUser.setAccountExpired(true);
        oldUser.setAccountLocked(true);
        oldUser.setCredentialsExpired(true);
        oldUser.setEnabled(false);

        Authority oldAuthority = new Authority();
        oldAuthority.setId(1L);
        oldUser.setAuthority(oldAuthority);

        Mockito.when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.of(oldUser));

        Mockito.when(userRepositoryMock.save(testUser)).thenReturn(oldUser);

        User updatedUser = userService.update(testUser);

        assert updatedUser.getId().equals(testUser.getId());
        assert updatedUser.getUsername().equals(testUser.getUsername());
        assert !(updatedUser.getPassword().equals(testUser.getPassword())) : "Password need to stay same(NOT CHANGE PASSWORD IN UPDATE)";
        assert updatedUser.isAccountExpired() == testUser.isAccountExpired();
        assert updatedUser.isAccountLocked() == testUser.isAccountLocked();
        assert updatedUser.isCredentialsExpired() == testUser.isCredentialsExpired();
        assert updatedUser.isEnabled() == testUser.isEnabled();
        assert updatedUser.getAuthority() != testUser.getAuthority();
        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(testUser);
    }

    @Test
    public void whenTryToUpdateUserThatNotExist_thenShouldThrowEntityNotExistException(){

        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("user@test.com");

        Mockito.when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.ofNullable(null));

        EntityNotExistException entityNotExistException = null;
        try {
            userService.update(testUser);
        }catch (EntityNotExistException e){
            entityNotExistException = e;
        }

        assert entityNotExistException != null;
        assert entityNotExistException.getMessage().equals("api.error.user.notExist");
        Mockito.verify(userRepositoryMock, Mockito.times(0)).save(testUser);
    }

    @Test
    public void whenTryToChangePasswordWithValidData_thenPasswordShouldBeUpdated(){

        PasswordEncoder userPasswordEncoder = new Encoders().userPasswordEncoder();

        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String oldPasswordHash = userPasswordEncoder.encode(oldPassword);

        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword(oldPassword);
        testUser.setNewPassword(newPassword);
        testUser.setConfirmNewPassword(newPassword);

        User oldUser = new User();
        oldUser.setId(1L);
        oldUser.setPassword(oldPasswordHash);

        Mockito.when(encodersMock.userPasswordEncoder()).thenReturn(userPasswordEncoder);
        Mockito.when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.of(oldUser));

        Exception exception = null;
        try{
             userService.changePassword(testUser);
        }catch (Exception e){
            exception = e;
        }

        assert exception == null;
        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(testUser);
    }

    @Test
    public void whenTryToChangePasswordWithWrongOldPassword_thenShouldThrowMethodArgumentNotValidException(){

        PasswordEncoder userPasswordEncoder = new Encoders().userPasswordEncoder();

        String oldPassword = "oldPassword";
        String oldWrongPassword = "oldWrongPassword";
        String newPassword = "newPassword";
        String oldPasswordHash = userPasswordEncoder.encode(oldPassword);

        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword(oldWrongPassword);
        testUser.setNewPassword(newPassword);
        testUser.setConfirmNewPassword(newPassword);

        User oldUser = new User();
        oldUser.setId(1L);
        oldUser.setPassword(oldPasswordHash);

        Mockito.when(encodersMock.userPasswordEncoder()).thenReturn(userPasswordEncoder);
        Mockito.when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.of(oldUser));

        Exception methodArgumentNotValidException = null;
        try{
            userService.changePassword(testUser);
        }catch (Exception e){
            methodArgumentNotValidException = e;
        }

        assert methodArgumentNotValidException != null;
        Mockito.verify(userRepositoryMock, Mockito.times(0)).save(testUser);
    }

    @Test
    public void whenTryToFindUserWithExistingId_thenShouldReturnUser(){

        User testUser = new User();
        testUser.setId(1L);

        Mockito.when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        User findUser = userService.find(testUser.getId());

        assert findUser != null;
    }

    @Test
    public void whenTryToFindUserWithNotExistingId_thenShouldThrowEntityNotExistException(){

        User testUser = new User();
        testUser.setId(1L);

        Mockito.when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.ofNullable(null));

        EntityNotExistException entityNotExistException = null;
        try {
            userService.find(testUser.getId());
        }catch (EntityNotExistException e){
            entityNotExistException = e;
        }

        assert entityNotExistException != null;
        assert entityNotExistException.getMessage().equals("api.error.user.notExist");
    }

    @Test
    public void whenTryToFindUsers_thenShouldReturnThem(){

        User testUser1 = new User();
        testUser1.setId(1L);

        PageRequest pageable = PageRequest.of(1,1);


        PageImpl<User> usersPage = new PageImpl(List.of(testUser1),pageable,3L);

        Mockito.when(userRepositoryMock.findAll(pageable)).thenReturn(usersPage);

        Page<User> users = userService.findAll(pageable);
        assert users.getTotalElements() == 3;
        assert users.getSize() == 1;
        assert users.getTotalPages() == 3;
    }

    @Test
    public void whenTryToDeleteUserWithValidId_thenShouldDeleteThem(){

        User testUser = new User();
        testUser.setId(1L);

        userService.delete(testUser.getId());

        Mockito.verify(userRepositoryMock, Mockito.times(1)).deleteById(testUser.getId());
    }

    @Test
    public void whenTryToDeleteUserWithInvalidId_thenShouldThrowEntityNotExistException(){

        User testUser = new User();
        testUser.setId(1L);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(userRepositoryMock).deleteById(testUser.getId());

        EntityNotExistException entityNotExistException=null;
        try{
            userService.delete(testUser.getId());
        }catch (EntityNotExistException e){
            entityNotExistException = e;
        }

        assert entityNotExistException != null;
        assert entityNotExistException.getMessage().equals("api.error.user.notExist");
        Mockito.verify(userRepositoryMock, Mockito.times(1)).deleteById(testUser.getId());
    }

}