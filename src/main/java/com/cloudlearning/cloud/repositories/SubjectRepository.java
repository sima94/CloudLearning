package com.cloudlearning.cloud.repositories;

import com.cloudlearning.cloud.models.Subject;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeleteCrudRepository;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeletePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface SubjectRepository extends SoftDeleteCrudRepository<Subject, Long>, SoftDeletePagingAndSortingRepository<Subject, Long> {

    @Transactional
    @Query("select e from #{#entityName} e where e.isDeleted = false and e.professor.id = ?1")
    Page<Subject> findSubjectsByProfessorId(Long professorId, Pageable pageable);

    @Transactional
    @Query("select e from #{#entityName} e left join e.students s where e.isDeleted = false and s.isDeleted = false and s.id = ?1  ")
    Page<Subject> findSubjectsByStudentsId(Long studentId, Pageable pageable);
}
