package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.helpers.AppConstant;
import com.scm.helpers.Helper;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repo.UserRepo;
import com.scm.services.EmailService;
import com.scm.services.UserService;

@Service 
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, EmailService emailService){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public User saveUser(User user) {
        user.setUserId(UUID.randomUUID().toString());

        //password encoder
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //setting user role
        user.setRoleList(List.of(AppConstant.ROLE_USER));

        
        String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        User savedUser = userRepo.save(user);
        String emailLink = Helper.getLinkForEmailVerification(emailToken); 
        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart Contact Manager", emailLink);
        
        return savedUser;
   }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public User updateUser(User user) {
       User user2 = userRepo.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException("User not found"));
       userRepo.delete(user2);
       return userRepo.save(user);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));
          userRepo.delete(user);
    }

    @Override
    public boolean isUserExistById(String id) {
        User user = userRepo.findById(id).orElse(null);
        return user != null; 
    }
    
    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null; 
        
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        
        return userRepo.findByEmail(email).orElse(null);
    }

    
}
