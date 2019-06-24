package com.cloudlearning.cloud.services.user;

import com.cloudlearning.cloud.exeptions.entity.EntityException;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.exeptions.entity.EntityNotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User find(Long id) throws EntityNotExistException;

    Page<User> findAll(Pageable pageable);

    User create(User user);

    User update(User user);

    void changePassword(User user) throws Exception;

    void delete(Long id) throws EntityNotExistException;
}
