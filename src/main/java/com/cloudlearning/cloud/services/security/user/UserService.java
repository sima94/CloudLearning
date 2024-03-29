package com.cloudlearning.cloud.services.security.user;

import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User find(Long id) throws EntityNotExistException;

    Page<User> findAll(Pageable pageable);

    User create(User user);

    User update(User user);

    void changePassword(User user) throws Exception;

    void delete(Long id);
}
