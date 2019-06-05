package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.exeptions.EntityExistExeption;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.exeptions.EntityNotFoundException;
import com.cloudlearning.cloud.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/api/v1/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public User getUser(@PathVariable Long id){
        try {
            return userService.find(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @GetMapping(path = "/api/v1/user/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public User getMe(OAuth2Authentication principal){
        User user = (User)principal.getUserAuthentication().getPrincipal();
        return user;
    }

    @GetMapping(path = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public Page<User> getUsers(@PageableDefault(value = 100) Pageable pageable){
        return userService.findAll(pageable);
    }

    @PostMapping(path = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public User createUser(@RequestBody User user){
        try {
            return userService.create(user);
        } catch (EntityExistExeption e) {
            throw new ResponseStatusException(
                    HttpStatus.EXPECTATION_FAILED, "User with that username exist", e);
        }
    }

    @PutMapping(path = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public User updateUser(@RequestBody User user){
        return userService.update(user);
    }

    @DeleteMapping(path = "/api/v1/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Long id){
        try {
            userService.delete(id);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found", e);
        }
    }
}
