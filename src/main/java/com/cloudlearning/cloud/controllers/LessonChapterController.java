package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.models.Lesson;
import com.cloudlearning.cloud.models.LessonChapter;
import com.cloudlearning.cloud.services.lessonChapter.LessonChapterService;
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
public class LessonChapterController {

    @Autowired
    LessonChapterService lessonChapterService;

    @GetMapping(path = "/api/v1/lesson/{id}/chapters", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Page<LessonChapter> getLessonChapters(@PathVariable Long id, @PageableDefault Pageable pageable) {
        return lessonChapterService.findLessonChapters(id, pageable);
    }

    @PostMapping(path = "/api/v1/lesson/{id}/chapter/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("hasRole('PROFESSOR')")
    public LessonChapter createLessonChapter(@PathVariable Long id, @Validated @RequestBody LessonChapter lessonChapter){
        Lesson lesson = new Lesson();
        lesson.setId(id);
        lessonChapter.setLesson(lesson);
        return lessonChapterService.create(lessonChapter);
    }

    @DeleteMapping(path = "/api/v1/lesson/chapter/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('PROFESSOR')")
    public void deleteLessonChapter(@PathVariable Long id){
        lessonChapterService.delete(id);
    }
}
