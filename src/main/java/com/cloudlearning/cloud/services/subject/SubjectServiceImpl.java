package com.cloudlearning.cloud.services.subject;

import com.cloudlearning.cloud.configuration.utils.authentication.AuthenticationFacade;
import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.Subject;
import com.cloudlearning.cloud.models.members.Professor;
import com.cloudlearning.cloud.models.members.student.Student;
import com.cloudlearning.cloud.repositories.SubjectRepository;
import com.cloudlearning.cloud.repositories.members.ProfessorRepository;
import com.cloudlearning.cloud.repositories.members.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public Boolean isSubjectOwner(Long id, String username) {
        Subject subject = subjectRepository.findById(id).orElseThrow( ()-> new EntityNotExistException("api.error.subject.notFound"));
        return subject.getProfessor().getUser().getUsername().equals(username);
    }

    @Override
    public Subject findById(Long id) {
        return subjectRepository.findById(id).orElseThrow(()-> new EntityNotExistException("api.error.subject.notExist"));
    }

    @Override
    public Page<Subject> findAll(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    @Override
    public Page<Subject> findAllForProfessor(Long id, Pageable pageable) {
        return subjectRepository.findSubjectsByProfessorId(id, pageable);
    }

    @Override
    public Page<Subject> findAllForStudent(Long id, Pageable pageable) {
        return subjectRepository.findSubjectsByStudentsId(id, pageable);
    }

    @Override
    public Subject create(Subject subject) {

        Professor professor = professorRepository.findById(subject.getProfessorId()).orElseThrow(()-> new EntityNotExistException("api.error.professor.notExist"));
        subject.setProfessor(professor);
        return subjectRepository.save(subject);
    }

    @Override
    public void connectStudentWithSubject(Long subjectId) {

        String loginUsername = authenticationFacade.getAuthentication().getName();
        Student student = studentRepository.findStudentByUserUsername(loginUsername).orElseThrow(()->new EntityNotExistException("api.error.student.notExist"));
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(()->new EntityNotExistException("api.error.subject.notExist"));

        subject.getStudents().add(student);
        subjectRepository.save(subject);
    }

    @Override
    public void delete(Long id) {
        try {
            subjectRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EntityNotExistException("api.error.subject.notExist");
        }
    }
}
