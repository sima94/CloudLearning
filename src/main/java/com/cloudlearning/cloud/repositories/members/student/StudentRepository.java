package com.cloudlearning.cloud.repositories.members.student;

import com.cloudlearning.cloud.models.members.student.Student;
import com.cloudlearning.cloud.repositories.members.MemberBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MemberBaseRepository<Student,Long> {

    Optional<Student> findStudentByUserUsername(String username);
}
