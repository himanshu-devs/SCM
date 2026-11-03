package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> getAll();

    Contact getById(String id);

    void delete(User user, String id);

    Page<Contact> searchByName(User user,String name, int page, int size, String sortBy, String direction);

    Page<Contact> searchByEmail(User user, String Email, int page, int size, String sortBy, String direction);

    Page<Contact> searchByPhoneNumber(User user, String phoneNumber, int page, int size, String sortBy, String direction);

    List<Contact> getByUserID(String userId);
    
    Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction);

}
