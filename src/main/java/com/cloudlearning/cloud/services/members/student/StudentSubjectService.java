package com.cloudlearning.cloud.services.members.student;

import com.cloudlearning.cloud.models.members.student.StudentSubject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentSubjectService {

    void changeApproveStatusForStudentSubject(Long id, Boolean approve);

    void deleteStudentSubject(Long id);

    Page<StudentSubject> findAllUnapprovedForSubjectId(Long subjectId, Pageable pageable);

}
