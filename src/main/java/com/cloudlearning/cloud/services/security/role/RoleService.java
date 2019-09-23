package com.cloudlearning.cloud.services.security.role;

import com.cloudlearning.cloud.models.security.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {

    Page<Role> findAll(Pageable pageable);
}
