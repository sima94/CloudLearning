package com.cloudlearning.cloud.services.members.student;

import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.members.student.StudentSubject;
import com.cloudlearning.cloud.repositories.members.student.StudentSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class StudentSubjectServiceImpl implements StudentSubjectService {

    @Autowired
    StudentSubjectRepository studentSubjectRepository;

    public Boolean isStudentSubjectOwner(Long id, String username) {
        StudentSubject studentSubject = studentSubjectRepository.findById(id).orElseThrow( ()-> new EntityNotExistException("api.error.studentSubject.notFound"));
        return studentSubject.getSubject().getProfessor().getUser().getUsername().equals(username);
    }

    @Override
    @PreAuthorize("@studentSubjectServiceImpl.isStudentSubjectOwner(#id, authentication.name)")
    public void changeApproveStatusForStudentSubject(Long id, Boolean approve){
        StudentSubject studentSubject = studentSubjectRepository.findById(id).orElseThrow( ()-> new EntityNotExistException("api.error.studentSubject.notFound"));
        studentSubject.setIsApproved(approve);
        studentSubjectRepository.save(studentSubject);
    }

    @Override
    @PreAuthorize("@studentSubjectServiceImpl.isStudentSubjectOwner(#id, authentication.name)")
    public void deleteStudentSubject(Long id) {
        studentSubjectRepository.deleteById(id);
    }

    @Override
    public Page<StudentSubject> findAllUnapprovedForSubjectId(Long subjectId, Pageable pageable) {
        return studentSubjectRepository.findAllUnapprovedBySubjectId(subjectId, pageable);
    }
}
