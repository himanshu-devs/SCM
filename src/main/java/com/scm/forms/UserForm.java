package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserForm {
    @NotBlank(message = "Username is required")
    @Size (min = 3, max = 50, message = "min 3 and max 50 characters")
    private String name;
    @Email (message = "Email should be valid")
    @NotBlank (message = "Email is required")
    // @Pattern 
    private String email;
    @NotBlank 
    @Size (min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;
    @NotBlank(message = "About is required")
    private String about;
    @Size (min = 10, max = 15, message = "Invalid Phone number")
    private String phoneNumber;

    private MultipartFile profileImage;

}
