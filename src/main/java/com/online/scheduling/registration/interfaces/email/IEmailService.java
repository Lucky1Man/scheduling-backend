package com.online.scheduling.registration.interfaces.email;

import javax.mail.MessagingException;

public interface IEmailService {
    void send(String to, String subject, String message) throws MessagingException;
}