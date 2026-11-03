package com.scm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.scm.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${app.mail.from:test@example.com}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(fromEmail);

        emailSender.send(message);
    }

    @Override
    public void sendEmailWithHtml() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void sendEmailWithAttachment() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}