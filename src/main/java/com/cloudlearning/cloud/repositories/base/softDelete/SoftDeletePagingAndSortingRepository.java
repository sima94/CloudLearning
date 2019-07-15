package com.cloudlearning.cloud.repositories.base.softDelete;

import com.cloudlearning.cloud.models.base.BasicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@NoRepositoryBean
public interface SoftDeletePagingAndSortingRepository<T extends BasicEntity, ID extends Long> extends PagingAndSortingRepository<T, ID> {

    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.isDeleted = false")
    Iterable<T> findAll(Sort sort);

    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.isDeleted = false")
    Page<T> findAll(Pageable pageable);

}
