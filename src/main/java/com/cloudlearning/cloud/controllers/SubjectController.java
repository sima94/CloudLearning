package com.cloudlearning.cloud.controllers;

import com.cloudlearning.cloud.models.Subject;
import com.cloudlearning.cloud.models.members.student.StudentSubject;
import com.cloudlearning.cloud.services.subject.SubjectService;
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
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @GetMapping(path = "/api/v1/subject/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Subject getSubject(@PathVariable Long id){
        return subjectService.findById(id);
    }

    @GetMapping(path = "/api/v1/subjects", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Page<Subject> getSubjects(@PageableDefault Pageable pageable){
        return subjectService.findAll(pageable);
    }

    @PostMapping(path = "/api/v1/subject/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public Subject createUpdate(@Validated(value = {Subject.ValidationCreate.class}) @RequestBody Subject subject){
        return subjectService.create(subject);
    }

    @PostMapping(path = "/api/v1/student/subject/{subjectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() and hasRole('STUDENT')")
    public void connectStudentWithSubject(@PathVariable Long subjectId){
         subjectService.connectStudentWithSubject(subjectId);
    }

    @GetMapping(path = "/api/v1/professor/{professorId}/subjects", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Page<Subject> getSubjectsByProfessorId(@PathVariable Long professorId, @PageableDefault Pageable pageable){
        return subjectService.findAllForProfessor(professorId, pageable);
    }

    @GetMapping(path = "/api/v1/student/{studentId}/subjects", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Page<Subject> getSubjectsByStudentId(@PathVariable Long studentId, @PageableDefault Pageable pageable){
        return subjectService.findAllForStudent(studentId, pageable);
    }

}
