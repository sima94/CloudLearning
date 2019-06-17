package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.exeptions.entity.EntityAlreadyExistExeption;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.exeptions.entity.EntityNotExistException;
import com.cloudlearning.cloud.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public User getUser(@PathVariable Long id){
        try {
            return userService.find(id);
        } catch (EntityNotExistException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isFullyAuthenticated()")
    public UserDetails getMe(Principal principal){
         String username = principal.getName();
         UserDetails user = userService.loadUserByUsername(username);
         return user;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public Page<User> getUsers(@PageableDefault Pageable pageable){
        return userService.findAll(pageable);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User createUser(@Validated(User.ValidationCreate.class) @RequestBody User user){
        try {
            return userService.create(user);
        } catch (EntityNotExistException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (EntityAlreadyExistExeption e){
            throw new ResponseStatusException(
                    HttpStatus.EXPECTATION_FAILED, e.getMessage(), e);
        }
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public User updateUser(@PathVariable Long id, @Validated(User.ValidationUpdate.class) @RequestBody User user){
        try {
            user.setId(id);
            return userService.update(user);
        } catch (EntityNotExistException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping(path = "/change/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Validated(User.ValidationChangePassword.class) @RequestBody User user, OAuth2Authentication principal) throws Exception {
        User oauthUser = (User)principal.getUserAuthentication().getPrincipal();
        user.setId(oauthUser.getId());
        userService.changePassword(user);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Long id){
        try {
            userService.delete(id);
        } catch (EntityNotExistException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
