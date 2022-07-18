package com.online.scheduling.registration.implementations.services;

import com.online.scheduling.registration.interfaces.IEmailService;
import lombok.Setter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;
    @Setter
    private String defaultFrom = "default@gmail.com";


    public EmailServiceImpl(
            JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void send(String to, String subject,String message, String from) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
        helper.setText(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(from);
        mailSender.send(mimeMessage);
    }

    @Override
    @Async
    public void send(String to, String subject,String message) throws MessagingException {
        send(to, subject, message, defaultFrom);
    }

}
