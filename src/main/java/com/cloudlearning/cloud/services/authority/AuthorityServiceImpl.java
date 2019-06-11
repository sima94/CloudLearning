package com.cloudlearning.cloud.services.authority;

import com.cloudlearning.cloud.models.security.Authority;
import com.cloudlearning.cloud.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Page<Authority> findAll(Pageable pageable) {
        return authorityRepository.findAll(pageable);
    }
}
