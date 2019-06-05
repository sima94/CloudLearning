package com.cloudlearning.cloud.services.user;

import com.cloudlearning.cloud.configuration.encryption.Encoders;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.repositories.UserRepository;
import com.cloudlearning.cloud.exeptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Encoders userPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return user;
        }

        throw new UsernameNotFoundException(username);
    }

    @Override
    public User create(User user) throws EntityExistExeption {

        List<User> existing = userRepository.findByUsernameOrId(user.getUsername(),user.getId());

        if (existing.size() > 0){
            throw new EntityExistExeption();
        }

        String password = user.getPassword();
        //Encrypt user password using BCryptPasswordEncoder(8)
        String passwordEncoded = userPasswordEncoder.userPasswordEncoder().encode(password);
        user.setPassword(passwordEncoded);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User find(Long id) throws EntityNotFoundException {
        User user = userRepository.findOne(id);

        if (user == null){
            throw new EntityNotFoundException();
        }

        return user;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {
        try {
            userRepository.delete(id);
        }catch (EmptyResultDataAccessException e){
            throw new EntityNotFoundException();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
