package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.repo.UserRepo;

@Controller 
@RequestMapping("/auth")
public class AuthController {

    @Autowired 
    private UserRepo userRepo;

    //verify email
    @GetMapping("/verify-email")
    public String verifyEmailToken(@RequestParam("token") String token){
        User user = userRepo.findByEmailToken(token).orElse(null);

        if(user != null){
            user.setEmailVerified(true);
            user.setEnabled(true);
            userRepo.save(user);
            return "success_page";
        }
        return "error_page";
    }
}
