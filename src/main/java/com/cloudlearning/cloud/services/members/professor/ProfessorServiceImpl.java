package com.cloudlearning.cloud.services.members.professor;

import com.cloudlearning.cloud.configuration.utils.authentication.AuthenticationFacade;
import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.members.Professor;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.repositories.members.ProfessorRepository;
import com.cloudlearning.cloud.repositories.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public Professor findById(Long id) {
        return professorRepository.findById(id).orElseThrow(() -> new EntityNotExistException("api.error.professor.notExist"));
    }

    @Override
    public Page<Professor> findAll(Pageable pageable) {
        return professorRepository.findAll(pageable);
    }

    @Override
    public Professor create(Professor professor) {

        String username = authenticationFacade.getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new EntityNotExistException("api.error.memberUser.notExist"));

        Professor oldProfessor = (Professor)user.getMember().orElse(professor);
        oldProfessor.remapFrom(professor);

        if (oldProfessor.getUser() == null){
            oldProfessor.setUser(user);
        }

        return professorRepository.save(oldProfessor);
    }
}
