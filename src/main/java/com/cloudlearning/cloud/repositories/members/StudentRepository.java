package com.cloudlearning.cloud.repositories.members;

import com.cloudlearning.cloud.models.members.Student;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MemberBaseRepository<Student,Long> {

    Optional<Student> findByUserUsername(String username);
}
