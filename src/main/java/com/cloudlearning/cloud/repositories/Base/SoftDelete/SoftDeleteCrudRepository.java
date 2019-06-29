package com.cloudlearning.cloud.repositories.Base.SoftDelete;

import com.cloudlearning.cloud.models.security.Base.BasicEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface SoftDeleteCrudRepository<T extends BasicEntity, ID extends Long> extends CrudRepository<T, ID> {
    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.isDeleted = false")
    List<T> findAll();

    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.id in ?1 and e.isDeleted = false")
    Iterable<T> findAllById(Iterable<ID> ids);

    @Override
    @Transactional
    @Query("select e from #{#entityName} e where e.id = ?1 and e.isDeleted = false")
    Optional<T> findById(ID id);

    //Look up deleted entities
    @Query("select e from #{#entityName} e where e.isDeleted = true")
    @Transactional
    List<T> findDeleted();

    @Override
    @Transactional
    @Query("select count(e) from #{#entityName} e where e.isDeleted = false")
    long count();

    @Override
    @Transactional
    default boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    @Query("update #{#entityName} e set e.isDeleted=true where e.id = ?1")
    @Transactional
    @Modifying
    void deleteById(ID id);

    @Override
    @Transactional
    default void delete(T entity) {
        deleteById((ID)entity.getId());
    }

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(entity -> deleteById((ID)entity.getId()));
    }

    @Override
    @Query("update #{#entityName} e set e.isDeleted=true ")
    @Transactional
    @Modifying
    void deleteAll();
}