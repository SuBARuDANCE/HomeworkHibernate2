package com.example.dao;

import com.example.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    User createUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    User updateUser(User user);
    boolean deleteUser(Long id);
    Optional<User> getUserByEmail(String email);
}