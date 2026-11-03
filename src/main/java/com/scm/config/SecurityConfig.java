package com.scm.config;



import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.config.Customizer;

import com.scm.entities.Provider;
import com.scm.entities.User;
import com.scm.helpers.AppConstant;
import com.scm.repo.UserRepo;
import com.scm.services.SecurityCustomUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration 
public class SecurityConfig {


    @Autowired 
    private AuthFailureHandler authFailureHandler;
    // @Bean 
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // }

    // @Bean
    // public UserDetailsService userDetailsService() {

    //    UserDetails user = User.withDefaultPasswordEncoder()
    // //    .withUsername("admin123")
    //    .username("admin123")
    //    .password("admin123")
    //    .roles("USER", "ADMIN").build(); 

    //    UserDetails user1 = User.withUsername("user123").password("password").build(); 

    //     var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user, user1);
    //     return inMemoryUserDetailsManager;
    // }

    private SecurityCustomUserDetailService securityCustomUserDetailService;
    private UserRepo userRepo;

    public SecurityConfig(SecurityCustomUserDetailService securityCustomUserDetailService, UserRepo userRepo){
        this.securityCustomUserDetailService = securityCustomUserDetailService;
        this.userRepo = userRepo;
    }

    @Bean 
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(securityCustomUserDetailService);
    
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http.csrf(e-> e.disable()).authorizeHttpRequests(request->{
            request.requestMatchers("/user/**").authenticated().anyRequest().permitAll();
        });

        http.formLogin(formLogin->{
            
            formLogin.loginPage("/login")
            .loginProcessingUrl("/authenticate")
            .defaultSuccessUrl("/user/dashboard")
            // .failureForwardUrl("/login?error=true")
            .usernameParameter("email")
            .passwordParameter("password");
            // .failureHandler(new AuthenticationFailureHandler() {

            //     @Override
            //     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            //             AuthenticationException exception) throws IOException, ServletException {
            //         // TODO Auto-generated method stub
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
            //     }
                
            // });

            // this is for enabling user using link send to email
            formLogin.failureHandler(authFailureHandler);
        });
    

        http.logout(logout->logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true").permitAll());

        // OAuth login
        http.oauth2Login(outh->{
            outh.loginPage("/login").successHandler(new AuthenticationSuccessHandler() {

                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        org.springframework.security.core.Authentication authentication) throws IOException, ServletException {

                            //identifying which provider is used for login
                        // String provider = authentication.getAuthorities().toString();

                        
                        var oauth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;  
                        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
                        
                        DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal(); 
                        
                        
                        
                        
                        
                        
                        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
                            
                            
                            String name = user.getAttribute("name").toString();
                            String picture = user.getAttribute("picture").toString();
                            String Email = user.getAttribute("email").toString();
                            User checkUser = userRepo.findByEmail(Email).orElse(null);
                            
                            
                            // save user to database
                            if(checkUser == null){
                                User user1 = new User();
                                user1.setUserId(UUID.randomUUID().toString());
                                user1.setRoleList(List.of(AppConstant.ROLE_USER));
                                user1.setEmailVerified(true);
                                user1.setEnabled(true);
                                
                                user1.setEmail(Email);
                                user1.setName(name);
                                user1.setProfilePic(picture);
                                user1.setPassword("Password");
                                user1.setProvider(Provider.GOOGLE);
                                user1.setProviderUserId(user.getName());
                                user1.setAbout("This account is created using google");
                                userRepo.save(user1);
                            }
                        }
                        
                        else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){

                            String email = user.getAttribute("email") != null ? 
                                              user.getAttribute("email").toString() : user.getAttribute("login").toString() + "@gmail.com";

                            User checkUser = userRepo.findByEmail(email).orElse(null);
                            if(checkUser == null){
                                User user1 = new User();
                                user1.setUserId(UUID.randomUUID().toString());
                                user1.setRoleList(List.of(AppConstant.ROLE_USER));
                                user1.setEmailVerified(true);
                                user1.setEnabled(true);
                                user1.setEmail(email);


                                String name = user.getAttribute("login").toString();
                                String picture = user.getAttribute("avatar_url").toString();
                                user1.setName(name);
                                user1.setProfilePic(picture);
                                user1.setPassword("Password");
                                user1.setProvider(Provider.GITHUB);
                                user1.setProviderUserId(user.getName());
                                user1.setAbout("This account is created using github");
                                userRepo.save(user1);

                            }

                        }


                    // TODO Auto-generated method stub
                    response.sendRedirect("/user/dashboard");
                }
                
            });
        });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
}
