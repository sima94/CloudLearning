package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.models.security.Authority;
import com.cloudlearning.cloud.services.authority.AuthorityService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authority")
public class AuthorityController {

    @Autowired
    AuthorityService authorityService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public Page<Authority> getAuthority(@PageableDefault Pageable pageable){
        return authorityService.findAll(pageable);
    }

}
