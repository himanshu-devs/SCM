package com.scm.controller;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helpers.AppConstant;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.repo.UserRepo;
import com.scm.services.ContactService;
import com.scm.services.ContactImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

    private final ModelMapper modelMapper;
    private final ContactService contactService;
    private final UserService userService;
    private final ContactImageService imageService;

    public ContactController(
            ModelMapper modelMapper,
            ContactService contactService,
            UserService userService,
            ContactImageService imageService) {

        this.modelMapper = modelMapper;
        this.contactService = contactService;
        this.userService = userService;
        this.imageService = imageService;
    }

    // add contact page
    @RequestMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult,
            Authentication authentication, HttpSession session) {

        if (bindingResult.hasErrors()) {

            Message message = Message.builder().content("Please correct the following errors.")
                    .type(MessageType.red).build();
            session.setAttribute("message", message);
            return "user/add_contact";
        }
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        Contact contact = new Contact();

        // System.out.println("image info: " +
        // contactForm.getContactImage().getOriginalFilename());

        // save image on cludinary and get url
        String fileName = null;
        String fileUrl = null;

        if (contactForm.getContactImage() != null
                && !contactForm.getContactImage().isEmpty()) {

            fileName = UUID.randomUUID().toString();

            fileUrl = imageService.uploadImage(
                    contactForm.getContactImage(), fileName);
        }

        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setFavorite(contactForm.isFavorite());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsitelink(contactForm.getWebsiteLink());
        contact.setCloudinaryImagePublicId(fileName);
        contact.setPicture(fileUrl);
        contact.setUser(user);
        contactService.save(contact);

        Message message = Message.builder().content("You have successfully added a new contact.")
                .type(MessageType.green).build();

        session.setAttribute("message", message);
        return "redirect:/user/contact/add";
    }

    @RequestMapping
    public String viewContacts(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContact", pageContact);

        return "user/contacts";
    }

    @GetMapping("/search")
    public String searchHandle(
            @RequestParam("field") String field,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE) int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        // Get the currently logged-in user
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact;

        // Validate the search field
        switch (field.toLowerCase()) {
            case "name":
                pageContact = contactService.searchByName(
                        user, keyword.trim(), page, size, sortBy, direction);
                break;

            case "email":
                pageContact = contactService.searchByEmail(
                        user, keyword.trim(), page, size, sortBy, direction);
                break;

            case "phone":
                pageContact = contactService.searchByPhoneNumber(
                        user, keyword.trim(), page, size, sortBy, direction);
                break;

            default:
                model.addAttribute("message", "Please select a valid search field.");
                pageContact = contactService.getByUser(
                        user, page, size, sortBy, direction);
        }

        // Send data to Thymeleaf
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("field", field);
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "user/contacts";
    }

    @RequestMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") String contactId, Authentication authentication,
            HttpSession session) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        contactService.delete(user, contactId);

        session.setAttribute("message",
                Message.builder().content("Contact is Deleted Successfully !!").type(MessageType.red).build());
        return "redirect:/user/contact";
    }

    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable("contactId") String contactId, Model model) {
        Contact contact = contactService.getById(contactId);

        ContactForm contactForm = modelMapper.map(contact, ContactForm.class);

        contactForm.setPicture(contact.getPicture());
        model.addAttribute("contactId", contactId);
        model.addAttribute("contactForm", contactForm);

        return "user/update_contact_view";
    }

    @PostMapping("/update/{contactId}")
    public String updateContact(@PathVariable("contactId") String contactId, @ModelAttribute ContactForm contactForm, HttpSession session){

        Contact contact = modelMapper.map(contactForm, Contact.class);

        if(contactForm.getContactImage() != null
                && !contactForm.getContactImage().isEmpty()) {

            String fileName = UUID.randomUUID().toString();

            String fileUrl = imageService.uploadImage(
                    contactForm.getContactImage(), fileName);
                    
            contact.setPicture(fileUrl);
        }
        contact.setId(contactId);

        contactService.update(contact);

        session.setAttribute("message", Message.builder().content("Contact Updated").type(MessageType.green).build());

        return "redirect:/user/contact/view/"+contactId;
    }
}
