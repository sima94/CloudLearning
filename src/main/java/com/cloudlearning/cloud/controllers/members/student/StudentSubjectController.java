package com.cloudlearning.cloud.controllers.members.student;

import com.cloudlearning.cloud.models.members.student.StudentSubject;
import com.cloudlearning.cloud.services.members.student.StudentSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentSubjectController {

    @Autowired
    StudentSubjectService studentSubjectService;

    @PostMapping("/api/v1/student/subjects/{studentSubjectId}/approve/{approve}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() AND hasRole('PROFESSOR')")
    public void changeStudentSubjectApproveStatus(@PathVariable Long studentSubjectId, @PathVariable Boolean approve) {
        studentSubjectService.changeApproveStatusForStudentSubject(studentSubjectId, approve);
    }

    @DeleteMapping(path = "/api/v1/student/subjects/{studentSubjectId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() AND hasRole('PROFESSOR')")
    public void deleteStudentSubject(@PathVariable Long studentSubjectId) {
        studentSubjectService.deleteStudentSubject(studentSubjectId);
    }

    @GetMapping(path = "/api/v1/student/subjects/unapproved/{subjectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated() AND hasRole('PROFESSOR')")
    public Page<StudentSubject> getStudentSubjectId(@PathVariable Long subjectId, @PageableDefault Pageable pageable){
        return studentSubjectService.findAllUnapprovedForSubjectId(subjectId, pageable);
    }
}
