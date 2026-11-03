package com.scm.controller;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ContactService;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private ContactService contactService;

    public UserController(UserService userService, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;
    }

    // user dashboard page
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {

        User user;

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {

            String email = oauthToken.getPrincipal().getAttribute("email");

            if (email == null || email.isBlank()) {
                return "redirect:/login?error=emailNotFound";
            }

            user = userService.getUserByEmail(email);

        } else {

            user = userService.getUserByEmail(authentication.getName());
        }

        if (user == null) {
            return "redirect:/login?error=userNotFound";
        }

        List<Contact> contacts = contactService.getByUserID(user.getUserId());

        long totalContacts = contacts.size();

        long favoriteContacts = contacts.stream()
                .filter(Contact::isFavorite)
                .count();

        List<Contact> recentContacts = contacts.stream()
                .sorted(Comparator.comparing(
                        Contact::getId,
                        Comparator.reverseOrder()))
                .limit(5)
                .toList();

        model.addAttribute("loggedInUser", user);
        model.addAttribute("totalContacts", totalContacts);
        model.addAttribute("favoriteContacts", favoriteContacts);
        model.addAttribute("recentContacts", recentContacts);

        return "user/dashboard";
    }

    @RequestMapping(value = "/profile")
    public String userProfile(Authentication authentication, Model model) {

        String email = authentication.getName();

        User user = userService.getUserByEmail(email);

        model.addAttribute("user", user);

        return "user/profile";

    }

    // user add contact page
    // user view contact page
    // user edit contact page
    // user delete contact page

}
