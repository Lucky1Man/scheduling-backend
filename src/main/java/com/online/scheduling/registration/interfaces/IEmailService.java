package com.online.scheduling.registration.interfaces;

import javax.mail.MessagingException;

public interface IEmailService {
    void send(String to, String subject, String message, String from) throws MessagingException;
    void send(String to, String subject, String message) throws MessagingException;
}