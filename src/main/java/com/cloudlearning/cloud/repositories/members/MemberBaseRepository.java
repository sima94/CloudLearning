package com.cloudlearning.cloud.repositories.members;

import com.cloudlearning.cloud.models.members.Member;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeleteCrudRepository;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeletePagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MemberBaseRepository<M extends Member, ID extends Long> extends SoftDeleteCrudRepository<M, ID>, SoftDeletePagingAndSortingRepository<M, ID> {

}