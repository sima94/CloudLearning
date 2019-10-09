package com.cloudlearning.cloud.services.lesson;

import com.cloudlearning.cloud.models.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LessonService {

    Boolean isLessonOwner(Long id, String username);

    Page<Lesson> findLessons(Long subjectId, Pageable pageable);

    Lesson create(Lesson lesson);

    void delete(Long id);
}
