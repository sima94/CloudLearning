package com.cloudlearning.cloud.services.members.member;

import com.cloudlearning.cloud.models.members.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    Member findById(Long id);

    Page<Member> findAll(Pageable pageable);

    Member create(Member member);

}
