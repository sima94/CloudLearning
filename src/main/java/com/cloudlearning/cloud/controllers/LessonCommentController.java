package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.models.LessonChapter;
import com.cloudlearning.cloud.models.LessonComment;
import com.cloudlearning.cloud.services.lessonComment.LessonCommentService;
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
public class LessonCommentController {

    @Autowired
    LessonCommentService lessonCommentService;

    @GetMapping(path = "/api/v1/lesson/chapter/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Page<LessonComment> getLessonChapterComments(@PathVariable Long id, @PageableDefault Pageable pageable) {
        return lessonCommentService.findAllLessonCommentsForLessonChapter(id, pageable);
    }

    @GetMapping(path = "/api/v1/lesson/comment/{id}/replays", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Page<LessonComment> getLessonCommentsReplays(@PathVariable Long id, @PageableDefault Pageable pageable) {
        return lessonCommentService.findAllLessonCommentsReplays(id, pageable);
    }

    @PostMapping(path = "/api/v1/lesson/chapter/{id}/comment/post", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("hasAnyRole('PROFESSOR', 'STUDENT')")
    public LessonComment createLessonComment(@PathVariable Long id, @Validated @RequestBody LessonComment lessonComment){
        LessonChapter lessonChapter = new LessonChapter();
        lessonChapter.setId(id);
        lessonComment.setLessonChapter(lessonChapter);
        return lessonCommentService.postLessonComment(lessonComment);
    }

    @PostMapping(path = "/api/v1/lesson/comment/{id}/replay", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("hasAnyRole('PROFESSOR', 'STUDENT')")
    public LessonComment replayLessonComment(@PathVariable Long id, @Validated @RequestBody LessonComment replayLessonComment){
        LessonComment lessonComment = new LessonComment();
        lessonComment.setId(id);
        replayLessonComment.setLessonComment(lessonComment);
        return lessonCommentService.replayLessonComment(replayLessonComment);
    }

    @DeleteMapping(path = "/api/v1/lesson/comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('PROFESSOR', 'STUDENT')")
    public void deleteLessonChapter(@PathVariable Long id){
        lessonCommentService.delete(id);
    }

}
