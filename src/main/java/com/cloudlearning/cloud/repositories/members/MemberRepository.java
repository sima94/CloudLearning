package com.cloudlearning.cloud.repositories.members;

import com.cloudlearning.cloud.models.members.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MemberBaseRepository<Member, Long> {

}
