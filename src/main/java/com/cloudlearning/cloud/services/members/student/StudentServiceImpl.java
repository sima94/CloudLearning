package com.cloudlearning.cloud.services.members.student;

import com.cloudlearning.cloud.configuration.utils.authentication.AuthenticationFacade;
import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.members.student.Student;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.repositories.members.student.StudentRepository;
import com.cloudlearning.cloud.repositories.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(()-> new EntityNotExistException("api.error.student.notExist"));
    }

    @Override
    public Page<Student> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    public Student create(Student student) {

        String username = authenticationFacade.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new EntityNotExistException("api.error.memberUser.notExist"));

        Student  oldStudent = (Student)user.getMember().orElse(student);
        oldStudent.remapFrom(student);

        if (oldStudent.getUser() == null){
            oldStudent.setUser(user);
        }

        return studentRepository.save(oldStudent);
    }
}
