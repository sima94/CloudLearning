package com.cloudlearning.cloud.services.subject;

import com.cloudlearning.cloud.models.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubjectService {

    Subject findById(Long id);

    Page<Subject> findAll(Pageable pageable);

    Page<Subject> findAllForProfessor(Long id, Pageable pageable);

    Page<Subject> findAllForStudent(Long id, Pageable pageable);

    Subject create(Subject subject);

    void connectStudentWithSubject(Long subjectId);

    void delete(Long id);

}
