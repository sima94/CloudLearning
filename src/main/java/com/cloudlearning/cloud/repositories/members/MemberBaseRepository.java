package com.cloudlearning.cloud.repositories.members;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface MemberBaseRepository<Member, ID> extends CrudRepository<Member, ID>, PagingAndSortingRepository<Member, ID> {

}