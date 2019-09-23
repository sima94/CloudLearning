package com.cloudlearning.cloud.repositories.security;

import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeleteCrudRepository;
import com.cloudlearning.cloud.repositories.base.softDelete.SoftDeletePagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends SoftDeleteCrudRepository<User, Long>, SoftDeletePagingAndSortingRepository<User, Long> {

    @Query("SELECT DISTINCT user FROM User user " +
            "INNER JOIN FETCH user.role AS role " +
            "WHERE user.username = :username"
            + " AND user.isDeleted = false")
    Optional<User> findByUsername(@Param("username") String username);

    List<User> findByUsernameOrId(@Param("username") String username, @Param("id") Long id);
}
