package com.cloudlearning.cloud.services.members.student;

import com.cloudlearning.cloud.models.members.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    Student findById(Long id);

    Page<Student> findAll(Pageable pageable);

    Student create(Student professor);
}
