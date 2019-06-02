package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.models.security.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class TestController {

    @GetMapping("/secured/test")
    public String securedTest() {
        return "Secured test";
    }

    @GetMapping("/test")
    public String test() {
        return "Test";
    }

    @RequestMapping(value = "/secured/user", method = RequestMethod.GET)
    @ResponseBody
    public UserDetails currentUserName(Principal principal) {
        UserDetails userDetails = (UserDetails) principal;
        return userDetails;
    }
}
