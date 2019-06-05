package com.cloudlearning.cloud.repositories;

import com.cloudlearning.cloud.models.security.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {

    @Query("SELECT DISTINCT user FROM User user " +
            "INNER JOIN FETCH user.authority AS authority " +
            "WHERE user.username = :username")
    User findByUsername(@Param("username") String username);

    List<User> findByUsernameOrId(@Param("username") String username, @Param("id") Long id);
}
