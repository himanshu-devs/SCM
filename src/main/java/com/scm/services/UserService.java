package com.scm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.scm.entities.User;

 
public interface UserService {
    User saveUser(User user);
    Optional<User> getUserById(String id);
    User getUserByEmail(String email);
    User updateUser(User user);
    void  deleteUser(String id);
    boolean isUserExistById(String id);
    boolean isUserExistByEmail(String email);
    List<User> getAllUsers();
}
