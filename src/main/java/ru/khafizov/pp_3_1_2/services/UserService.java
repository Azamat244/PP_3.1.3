package ru.khafizov.pp_3_1_2.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.khafizov.pp_3_1_2.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);

    void save(User user);

    List<User> findAll();

    User findById(Integer id);

    void validateUser(User user);

    void deleteById(Integer id);

    void updateUser(User updateUser);

    User findByEmail(String email);
}
