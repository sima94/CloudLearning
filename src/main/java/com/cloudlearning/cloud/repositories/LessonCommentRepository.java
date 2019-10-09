package com.cloudlearning.cloud.repositories;

import com.cloudlearning.cloud.models.LessonComment;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeleteCrudRepository;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeletePagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface LessonCommentRepository extends SoftDeleteCrudRepository<LessonComment, Long>, SoftDeletePagingAndSortingRepository<LessonComment, Long> {

    @Query("select e from #{#entityName} e where e.isDeleted = false and e.lessonChapter.id = ?1")
    Page<LessonComment> findAllByLessonChapterId(Long id, Pageable pageable);

    @Query("select e from #{#entityName} e where e.isDeleted = false and e.lessonComment.id = ?1")
    Page<LessonComment> findAllByLessonCommentId(Long id, Pageable pageable);

}
