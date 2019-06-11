package com.cloudlearning.cloud.services.authority;

import com.cloudlearning.cloud.models.security.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorityService {

    Page<Authority> findAll(Pageable pageable);
}
