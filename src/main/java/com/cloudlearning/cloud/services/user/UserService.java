package com.cloudlearning.cloud.services.user;

import com.cloudlearning.cloud.exeptions.entity.EntityAlreadyExistExeption;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.exeptions.entity.EntityNotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.MethodArgumentNotValidException;

public interface UserService extends UserDetailsService {

    User find(Long id) throws EntityNotExistException;

    Page<User> findAll(Pageable pageable);

    User create(User user) throws EntityAlreadyExistExeption;

    User update(User user);

    void changePassword(User user) throws Exception;

    void delete(Long id) throws EntityNotExistException;
}
