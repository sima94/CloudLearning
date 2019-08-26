package com.cloudlearning.cloud.services.security.user;

import com.cloudlearning.cloud.global.exception.entity.EntityAlreadyExistExeption;
import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.security.Role;
import com.cloudlearning.cloud.models.security.User;
import com.cloudlearning.cloud.repositories.security.UserRepository;
import com.cloudlearning.cloud.configuration.utils.encryption.Encoders;
import com.cloudlearning.cloud.repositories.security.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Encoders passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public User create(User user) {

        List<User> existing = userRepository.findByUsernameOrId(user.getUsername(),user.getId());

        if (existing.size() > 0){
            throw new EntityAlreadyExistExeption("api.error.user.usernameAlreadyExist");
        }

        Role role = roleRepository.findById(user.getRole().getId()).orElseThrow(()-> new EntityNotExistException("api.error.role.notExist"));
        user.setRole(role);

        String password = user.getPassword();
        //Encrypt user password using BCryptPasswordEncoder(8)
        String passwordEncoded = passwordEncoder.userPasswordEncoder().encode(password);
        user.setPassword(passwordEncoded);
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {

        User oldUser = userRepository.findById(user.getId()).orElseThrow(()-> new EntityNotExistException("api.error.user.notExist"));

        oldUser.setUsername(user.getUsername());
        oldUser.setAccountExpired(user.isAccountExpired());
        oldUser.setAccountLocked(user.isAccountLocked());
        oldUser.setCredentialsExpired(user.isCredentialsExpired());
        oldUser.setEnabled(user.isEnabled());

        return userRepository.save(oldUser);
    }

    @Override
    public void changePassword(User user) throws Exception {

        User oldUser = userRepository.findByUsername(user.getUsername()).get();

        if (!passwordEncoder.userPasswordEncoder().matches(user.getPassword(), oldUser.getPassword())){
            BeanPropertyBindingResult result = new BeanPropertyBindingResult(user, "user");
            FieldError fieldError = new FieldError("user", "password", user.getPassword(), true, null, null, "api.error.validation.invalidPassword");
            result.addError(fieldError);
            throw new MethodArgumentNotValidException(
                    new MethodParameter(this.getClass().getDeclaredMethod("changePassword", User.class), 0),
                    result);
        }

        String newPassword = user.getNewPassword();
        String newPasswordEncoded = passwordEncoder.userPasswordEncoder().encode(newPassword);
        oldUser.setPassword(newPasswordEncoded);
        userRepository.save(oldUser);
    }

    @Override
    public User find(Long id) throws EntityNotExistException {
        return userRepository.findById(id).orElseThrow(()-> new EntityNotExistException("api.error.user.notExist"));
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) throws EntityNotExistException {
        try {
            userRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EntityNotExistException("api.error.user.notExist");
        }
    }
}
