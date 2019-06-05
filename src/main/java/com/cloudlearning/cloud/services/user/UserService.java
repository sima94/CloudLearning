package com.cloudlearning.cloud.services.user;

import com.cloudlearning.cloud.exeptions.EntityExistExeption;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.exeptions.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User find(Long id) throws EntityNotFoundException;

    Page<User> findAll(Pageable pageable);

    User create(User user) throws EntityExistExeption;

    User update(User user);

    void delete(Long id) throws EntityNotFoundException;
}
