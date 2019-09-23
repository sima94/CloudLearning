package com.cloudlearning.cloud.services.members.member;

import com.cloudlearning.cloud.configuration.utils.authentication.AuthenticationFacade;
import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.members.Member;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.repositories.members.MemberRepository;
import com.cloudlearning.cloud.repositories.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(()-> new EntityNotExistException("api.error.member.notExist"));
    }

    @Override
    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Override
    public Member create(Member member) {

        String username = authenticationFacade.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new EntityNotExistException("api.error.memberUser.notExist"));
        member.setUser(user);

        return memberRepository.save(member);
    }
}
