package com.cloudlearning.cloud.services.lesson;

import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.Lesson;
import com.cloudlearning.cloud.repositories.LessonRepository;
import com.cloudlearning.cloud.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    SubjectRepository subjectRepository;

    public Boolean isLessonOwner(Long id, String username) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow( ()-> new EntityNotExistException("api.error.lesson.notFound"));
        return lesson.getSubject().getProfessor().getUser().getUsername().equals(username);
    }

    @Override
    public Page<Lesson> findLessons(Long subjectId, Pageable pageable) {
        return lessonRepository.findAllBySubjectId(subjectId, pageable);
    }

    @Override
    @PreAuthorize("@subjectServiceImpl.isSubjectOwner(#lesson.subject.id, authentication.name)")
    public Lesson create(Lesson lesson) {

        subjectRepository.findById(lesson.getSubject().getId()).orElseThrow(()-> new EntityNotExistException("api.error.subject.notFound"));
        lessonRepository.save(lesson);
        return lessonRepository.findById(lesson.getId()).orElse(lesson);
    }

    @Override
    @PreAuthorize("@lessonServiceImpl.isLessonOwner(#id, authentication.name)")
    public void delete(Long id) {
        try {
            lessonRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EntityNotExistException("api.error.lesson.notFound");
        }

    }
}
