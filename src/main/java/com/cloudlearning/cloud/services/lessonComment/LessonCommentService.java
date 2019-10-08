package com.cloudlearning.cloud.services.lessonComment;

import com.cloudlearning.cloud.models.LessonComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LessonCommentService {

    Boolean isLessonCommentOwner(Long id, String username);

    Page<LessonComment> findAllLessonCommentsForLessonChapter(Long lessonChapterId, Pageable pageable);

    Page<LessonComment> findAllLessonCommentsReplays(Long lessonCommentId, Pageable pageable);

    LessonComment replayLessonComment(LessonComment lessonComment);

    LessonComment postLessonComment(LessonComment lessonComment);

    void delete(Long id);

}
