package com.cloudlearning.cloud.services.lessonChapter;

import com.cloudlearning.cloud.models.LessonChapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LessonChapterService {

    Boolean isLessonChapterOwner(Long id, String username);

    Page<LessonChapter> findLessonChapters(Long lessonId, Pageable pageable);

    LessonChapter create(LessonChapter lessonChapter);

    void delete(Long id);
}
