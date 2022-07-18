package com.online.scheduling.registration.implementations.services;

import com.online.scheduling.registration.interfaces.IEmailService;

import javax.mail.MessagingException;

public class EmailServiceImplBlank implements IEmailService {
    @Override
    public void send(String to, String subject, String message, String from) throws MessagingException {
    }

    @Override
    public void send(String to, String subject, String message) throws MessagingException {
    }
}
