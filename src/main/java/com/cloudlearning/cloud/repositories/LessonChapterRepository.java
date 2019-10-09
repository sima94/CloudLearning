package com.cloudlearning.cloud.repositories;

import com.cloudlearning.cloud.models.LessonChapter;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeleteCrudRepository;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeletePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface LessonChapterRepository extends SoftDeleteCrudRepository<LessonChapter, Long>, SoftDeletePagingAndSortingRepository<LessonChapter, Long> {

    @Query("select e from #{#entityName} e where e.isDeleted = false and e.lesson.id = ?1")
    Page<LessonChapter> findAllByLessonId(Long id, Pageable pageable);
}
