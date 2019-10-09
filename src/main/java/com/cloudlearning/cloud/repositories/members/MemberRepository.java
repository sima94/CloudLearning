package com.cloudlearning.cloud.repositories.members;

import com.cloudlearning.cloud.models.members.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends MemberBaseRepository<Member, Long> {

    @Query("select e from #{#entityName} e where e.isDeleted = false and e.user.username = ?1")
    Optional<Member> findByUserUsername(String username);
}
