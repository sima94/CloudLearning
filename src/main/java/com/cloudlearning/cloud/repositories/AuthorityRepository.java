package com.cloudlearning.cloud.repositories;

import com.cloudlearning.cloud.models.security.Authority;
import com.cloudlearning.cloud.models.security.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long>, PagingAndSortingRepository<Authority, Long> {

}
