package com.cloudlearning.cloud.controllers.members;

import com.cloudlearning.cloud.models.members.Professor;
import com.cloudlearning.cloud.services.members.professor.ProfessorService;
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
@RequestMapping("/api/v1/professor")
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Professor getProfessor(@PathVariable Long id){
        return professorService.findById(id);
    }

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("isAuthenticated() AND hasRole('PROFESSOR')")
    public Professor createUpdate(@Validated @RequestBody Professor professor){
        return professorService.create(professor);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Page<Professor> getProfessors(@PageableDefault Pageable pageable){
        return professorService.findAll(pageable);
    }

}
