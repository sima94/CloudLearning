package com.cloudlearning.cloud.repositories;

import com.cloudlearning.cloud.configuration.base.AbstractTests;
import com.cloudlearning.cloud.models.security.Role;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.repositories.security.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

@DataJpaTest
public class UserRepositoryTests extends AbstractTests {

    @Autowired
    UserRepository userRepository;

    @Test
    public void whenFindByUsername_thenReturnUser() {
        // given
        User admin = new User();
        admin.setId(1L);
        admin.setUsername("admin@test.com");
        Role adminRole = new Role();
        adminRole.setId(1L);
        admin.setRole(adminRole);

        userRepository.save(admin);

        // when
        User found = userRepository.findByUsername("admin@test.com").get();

        // then
        assert admin.getUsername().equals(found.getUsername());
    }

    @Test
    public void whenFindByUsername_andUserIsSoftDeleted_thenDoNotReturnUser() {
        // given
        User admin = new User();
        admin.setId(1L);
        admin.setUsername("admin@test.com");
        Role adminRole = new Role();
        adminRole.setId(1L);
        admin.setDeleted(true);
        admin.setRole(adminRole);

        // when
        userRepository.save(admin);

        // then
        assert !userRepository.findByUsername("admin@test.com").isPresent();
    }

    @Test
    public void whenFindByUsernameOrId_thenReturnUsers() {

        // given
        User admin = new User();
        admin.setId(1L);
        admin.setUsername("admin@test.com");
        Role adminRole = new Role();
        adminRole.setId(1L);
        admin.setRole(adminRole);

        User professor = new User();
        professor.setId(2L);
        professor.setUsername("profesor@test.com");
        Role professorRole = new Role();
        professorRole.setId(2L);
        professor.setRole(professorRole);

        // when
        userRepository.save(admin);
        userRepository.save(professor);

        // then
        List<User> users = userRepository.findByUsernameOrId(admin.getUsername(),professor.getId());
        assert users.size() == 2;
    }
}
