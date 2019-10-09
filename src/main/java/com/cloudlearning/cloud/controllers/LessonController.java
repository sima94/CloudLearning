package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.models.Lesson;
import com.cloudlearning.cloud.models.Subject;
import com.cloudlearning.cloud.services.lesson.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class LessonController {

    @Autowired
    LessonService lessonService;

    @GetMapping(path = "/api/v1/subject/{id}/lessons", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Page<Lesson> getLesson(@PathVariable Long id, @PageableDefault Pageable pageable) {
        return lessonService.findLessons(id, pageable);
    }

    @PostMapping(path = "/api/v1/subject/{id}/lesson/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("hasRole('PROFESSOR')")
    public Lesson createLesson(@PathVariable Long id, @Validated @RequestBody Lesson lesson){
        Subject subject = new Subject();
        subject.setId(id);
        lesson.setSubject(subject);
        return lessonService.create(lesson);
    }

    @DeleteMapping(path = "/api/v1/lesson/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PROFESSOR')")
    public void deleteLesson(@PathVariable Long id){
        lessonService.delete(id);
    }

}
