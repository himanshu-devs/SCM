package com.scm.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @Autowired 
    private ContactImageService imageService;

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");

        model.addAttribute("name", "Substring technologies"); // now with the help of noe name or channel can be
                                                              // accessed in view
        model.addAttribute("youtubeChannel", "LaughingHub");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage() {
        System.out.println("About page loading");
        return "about";
    }

    @RequestMapping("/services")
    public String aboutServices() {
        System.out.println("Service page loading");
        return "services";
    }

    @RequestMapping("/contact")
    public String aboutContact() {
        System.out.println("Contact page loading");
        return "contact";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String aboutLogin(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        System.out.println("Login page loading");
        return "login";
    }

    @RequestMapping("/register")
    public String aboutSignup(Model model) {
        // System.out.println("Signup page loading");

        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);

        return "register";
    }

    // Processing register
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult,
            HttpSession session) {
        // System.out.println(userForm);
        // Fetch form data
        // Validate form data
        if (bindingResult.hasErrors()) {
            // System.out.println("Form has errors: " + bindingResult.getAllErrors());
            return "register";
        }
        if (userService.isUserExistByEmail(userForm.getEmail())) {
            bindingResult.rejectValue(
                    "email",
                    "email.exists",
                    "This email is already registered!");

            return "register";
        }

        String fileName = null;
        String fileUrl = null;

        if (userForm.getProfileImage() != null
                && !userForm.getProfileImage().isEmpty()) {

            fileName = UUID.randomUUID().toString();

            fileUrl = imageService.uploadImage(
                    userForm.getProfileImage(), fileName);
        }
        // save to database ->

        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .build();
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic(fileUrl);

        User savedUser = userService.saveUser(user);

        // message registration successfull
        Message message = Message.builder().content("Registration successful! Please login to continue.")
                .type(MessageType.green).build();
        session.setAttribute("message", message);
        // Redirect to login page
        return "redirect:/register";
    }
}
