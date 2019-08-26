package com.cloudlearning.cloud.controllers.members;

import com.cloudlearning.cloud.models.members.Professor;
import com.cloudlearning.cloud.models.members.Student;
import com.cloudlearning.cloud.services.members.student.StudentService;
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
@RequestMapping("/api/v1/student")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Student getStudent(@PathVariable Long id){
        return studentService.findById(id);
    }

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("isAuthenticated() AND hasRole('STUDENT')")
    public Student createUpdate(@Validated @RequestBody Student student){
        return studentService.create(student);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Page<Student> getStudents(@PageableDefault Pageable pageable){
        return studentService.findAll(pageable);
    }

}
