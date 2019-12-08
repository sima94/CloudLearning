package com.cloudlearning.cloud.services.lessonChapter;

import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.Lesson;
import com.cloudlearning.cloud.models.LessonChapter;
import com.cloudlearning.cloud.repositories.LessonChapterRepository;
import com.cloudlearning.cloud.repositories.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class LessonChapterServiceImpl implements LessonChapterService {

    @Autowired
    LessonChapterRepository lessonChapterRepository;

    @Autowired
    LessonRepository lessonRepository;

    public Boolean isLessonChapterOwner(Long id, String username) {
        LessonChapter lessonChapter = lessonChapterRepository.findById(id).orElseThrow( ()-> new EntityNotExistException("api.error.lessonChapter.notFound"));
        return lessonChapter.getLesson().getSubject().getProfessor().getUser().getUsername().equals(username);
    }

    @Override
    public Page<LessonChapter> findLessonChapters(Long lessonId, Pageable pageable) {
        return lessonChapterRepository.findAllByLessonId(lessonId, pageable);
    }

    @Override
    @PreAuthorize("@lessonServiceImpl.isLessonOwner(#lessonChapter.lesson.id, authentication.name)")
    public LessonChapter create(LessonChapter lessonChapter) {
        Lesson lesson = lessonRepository.findById(lessonChapter.getLesson().getId()).orElseThrow(()-> new EntityNotExistException("api.error.lesson.notFound"));
        lessonChapter.setLesson(lesson);
        lessonChapterRepository.save(lessonChapter);
        return lessonChapterRepository.findById(lessonChapter.getId()).orElse(lessonChapter);
    }

    @Override
    @PreAuthorize("@lessonChapterServiceImpl.isLessonChapterOwner(#id, authentication.name)")
    public void delete(Long id) {
        try {
            lessonChapterRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EntityNotExistException("api.error.lessonChapter.notFound");
        }
    }
}
