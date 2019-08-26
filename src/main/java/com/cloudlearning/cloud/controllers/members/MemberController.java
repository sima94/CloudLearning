package com.cloudlearning.cloud.controllers.members;

import com.cloudlearning.cloud.models.members.Member;
import com.cloudlearning.cloud.services.members.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Member getMember(@PathVariable Long id){
       return memberService.findById(id);
    }

//    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.CREATED)
//    @ResponseBody
//    public Member createUpdateMember(@Validated @RequestBody Member member){
//        return memberService.create(member);
//    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Page<Member> getMembers(@PageableDefault Pageable pageable){
        return memberService.findAll(pageable);
    }

}
