package com.cloudlearning.cloud.controllers.members;

import com.cloudlearning.cloud.models.members.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Member getMember(@PathVariable Long id){
       return new Member();
    }

    @GetMapping(path = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public Member createUpdateMember(@RequestBody Member member){
        return new Member();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Page<Member> getUsers(@PageableDefault Pageable pageable){
        return new PageImpl<Member>();
    }

}
