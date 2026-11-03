package com.scm.services.impl;

import com.scm.repo.UserRepo;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scm.entities.Contact;
import com.scm.entities.SocialLink;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repo.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServciceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact save(Contact contact) {

        String contactID = UUID.randomUUID().toString();
        contact.setId(contactID);
        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {

        Contact contactOld = contactRepo.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not Found"));

        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setWebsitelink(contact.getWebsitelink());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
        contactOld.getLinks().clear();

        if (contact.getLinks() != null) {
            for (SocialLink link : contact.getLinks()) {
                link.setContact(contactOld);
                contactOld.getLinks().add(link);
            }
        }
        
        if (contact.getPicture() != null) {
            contactOld.setPicture(contact.getPicture());
        }
        if (contact.getCloudinaryImagePublicId() != null) {
            contactOld.setCloudinaryImagePublicId(
                    contact.getCloudinaryImagePublicId());
        }

        return contactRepo.save(contactOld);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(User user, String id) {
        contactRepo.deleteByUserAndId(user, id);
    }

    @Override
    public List<Contact> getByUserID(String userId) {
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(User user, String name, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndNameContainingIgnoreCase(user, name, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(User user, String Email, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndEmailContainingIgnoreCase(user, Email, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(User user, String phoneNumber, int page, int size, String sortBy,
            String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumber, pageable);
    }

}
