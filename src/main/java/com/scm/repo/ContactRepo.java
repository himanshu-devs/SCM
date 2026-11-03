package com.scm.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;

import java.util.List;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    Page<Contact> findByUser(User user, Pageable pageable);

    @Query("select c from Contact c where c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByUserAndNameContainingIgnoreCase(
            User user, String name, Pageable pageable);

    Page<Contact> findByUserAndEmailContainingIgnoreCase(
            User user, String email, Pageable pageable);

    Page<Contact> findByUserAndPhoneNumberContaining(
            User user, String phoneNumber, Pageable pageable);

    void deleteByUserAndId(User user, String id);       
}
