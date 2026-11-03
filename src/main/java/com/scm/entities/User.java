package com.scm.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter // these are given because we use lambok
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    private String userId;
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Column(length = 6000)
    private String about;
    private String profilePic;
    private String gender;
    private String phoneNumber;


    
@Builder.Default
private boolean enabled = true;

@Builder.Default
private boolean emailVerified = false;

@Builder.Default
private boolean phoneVerified = false;

@Enumerated(value = EnumType.STRING)
@Builder.Default
private Provider provider = Provider.SELF;

private String providerUserId;

private String emailToken;

@OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
        fetch = FetchType.LAZY, orphanRemoval = true)
@Builder.Default
private List<Contact> contacts = new ArrayList<>();


@ElementCollection(fetch = FetchType.EAGER)
private List<String> roleList = new ArrayList<>();

@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    // TODO Auto-generated method stub
   Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
   return roles;
}

@Override
public String getUsername() {
    return this.email;
}

@Override 
public boolean isEnabled(){
    return this.enabled;
}

}
