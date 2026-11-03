package com.scm.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.repo.UserRepo;

@Service
public class SecurityCustomUserDetailService implements UserDetailsService {

    private final UserRepo userRepo;

    public SecurityCustomUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        System.out.println("Login email received: [" + username + "]");

        User user = userRepo.findByEmail(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Invalid username or password"));

        System.out.println("User found: " + user.getEmail());
        System.out.println("User enabled: " + user.isEnabled());

        return user;
    }
}
