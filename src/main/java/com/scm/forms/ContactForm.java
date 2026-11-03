package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
@ToString 
public class ContactForm {

    @NotBlank(message = "Name is Required")
    private String name;
    
    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid Email Address")
    private String email;
    
    @NotBlank(message = "Phone Number is Required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid Phone Number [ example@gmail.com ]")
    private String phoneNumber;
    
    @NotBlank(message = "Address is Required")
    private String address;

    private String description;

    private boolean favorite;

    private String websiteLink;

    private String linkedInLink;

    
    private MultipartFile contactImage;
    
    private String picture;
}
