package com.cloudlearning.cloud.repositories;

import com.cloudlearning.cloud.CloudApplicationTests;
import com.cloudlearning.cloud.models.security.Authority;
import com.cloudlearning.cloud.models.security.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTests extends CloudApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void whenFindByUsername_thenReturnUser() {
        // given
        User admin = new User();
        admin.setId(1L);
        admin.setUsername("admin@test.com");
        Authority adminAuth = new Authority();
        adminAuth.setId(1L);
        admin.setAuthority(adminAuth);

        userRepository.save(admin);

        // when
        User found = userRepository.findByUsername("admin@test.com").get();

        // then
        assert admin.getUsername().equals(found.getUsername());
    }

    @Test
    public void whenFindByUsernameOrId_thenReturnUsers() {

        User admin = new User();
        admin.setId(1L);
        admin.setUsername("admin@test.com");
        Authority adminAuth = new Authority();
        adminAuth.setId(1L);
        admin.setAuthority(adminAuth);

        User professor = new User();
        professor.setId(2L);
        professor.setUsername("profesor@test.com");
        Authority professorAuth = new Authority();
        professorAuth.setId(2L);
        professor.setAuthority(professorAuth);

        userRepository.save(admin);
        userRepository.save(professor);

        List<User> users = userRepository.findByUsernameOrId(admin.getUsername(),professor.getId());
        assert users.size() == 2;
    }
}
