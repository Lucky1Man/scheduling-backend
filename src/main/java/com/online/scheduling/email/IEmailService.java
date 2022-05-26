package com.online.scheduling.email;

import javax.mail.MessagingException;

public interface IEmailService {
    void send(String to, String subject, String message) throws MessagingException;
}