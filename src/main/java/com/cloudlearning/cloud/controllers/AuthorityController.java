package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.models.security.Authority;
import com.cloudlearning.cloud.services.authority.AuthorityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authority")
@AllArgsConstructor
public class AuthorityController {

    final private AuthorityService authorityService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Authority> getAuthority(@PageableDefault Pageable pageable){
        return authorityService.findAll(pageable);
    }

}
