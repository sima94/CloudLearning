package com.cloudlearning.cloud.repositories.members.student;

import com.cloudlearning.cloud.models.members.student.StudentSubject;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeleteCrudRepository;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeletePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface StudentSubjectRepository extends SoftDeleteCrudRepository<StudentSubject, Long>, SoftDeletePagingAndSortingRepository<StudentSubject, Long> {

    Optional<StudentSubject> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    @Transactional
    @Query("select e from #{#entityName} e where e.isApproved = false and e.subject.id = ?1 and e.isDeleted = false and e.subject.professor.user.username = ?#{#security.authentication.name}")
    Page<StudentSubject> findAllUnapprovedBySubjectId(Long subjectId, Pageable pageable);

}
