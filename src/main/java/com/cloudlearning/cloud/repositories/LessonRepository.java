package com.cloudlearning.cloud.repositories;

import com.cloudlearning.cloud.models.Lesson;

import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeleteCrudRepository;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeletePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface LessonRepository extends SoftDeleteCrudRepository<Lesson, Long>, SoftDeletePagingAndSortingRepository<Lesson, Long> {

    @Query("select e from #{#entityName} e where e.isDeleted = false and e.subject.id = ?1")
    Page<Lesson> findAllBySubjectId(Long id, Pageable pageable);
}
