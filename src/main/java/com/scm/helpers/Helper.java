package com.scm.helpers;

import java.security.Principal;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication){

        
        if(authentication instanceof OAuth2AuthenticationToken){
            
            var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
            String email = "";
            
            //if login with google
            if(authorizedClientRegistrationId.equalsIgnoreCase("google")) {
                email = oauth2AuthenticationToken.getPrincipal().getAttribute("email").toString();
                // System.out.println("login with google " + email);
            }
            //if login with github
            if(authorizedClientRegistrationId.equalsIgnoreCase("github")) {
                email = oauth2AuthenticationToken.getPrincipal().getAttribute("email") != null ? 
                                    oauth2AuthenticationToken.getPrincipal().getAttribute("email").toString() : 
                                        oauth2AuthenticationToken.getPrincipal().getAttribute("login").toString() + "@gmail.com";
                // System.out.println("login with github " + email);
            }
            return email;
        }
        else{
            //if login with email id and password
            // System.out.println("getting login with self " + authentication.getName().toString());
            return authentication.getName().toString();
        }
    }
    
    public static String getLinkForEmailVerification(String emailToken){
        String link = "http://localhost:8080/auth/verify-email?token=" + emailToken;
        return link;
    }

}
