package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.configuration.encryption.Encoders;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.beans.Encoder;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public User getUser(@PathVariable Long id){
        return userService.find(id);
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
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Page<User> getUsers(@PageableDefault Pageable pageable){
        return userService.findAll(pageable);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User createUser(@Validated(User.ValidationCreate.class) @RequestBody User user){
        return userService.create(user);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.OK)
    public User updateUser(@PathVariable Long id, @Validated(User.ValidationUpdate.class) @RequestBody User user){
        user.setId(id);
        return userService.update(user);
    }

    @PostMapping(path = "/change/password", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public void changePassword(@Validated(User.ValidationChangePassword.class) @RequestBody User user, Principal principal) throws Exception {
        String username = principal.getName();
        user.setUsername(username);
        userService.changePassword(user);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id){
        userService.delete(id);
    }

    @PostMapping(path = "/generate/password/{password}")
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String generatePasswordHash(@PathVariable String password) throws Exception {
        Encoders encoders = new Encoders();
        return encoders.userPasswordEncoder().encode(password);
    }
}
