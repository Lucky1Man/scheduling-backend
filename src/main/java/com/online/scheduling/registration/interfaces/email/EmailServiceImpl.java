package com.online.scheduling.registration.interfaces.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("EmailService")
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;
    private final EmailConfig config;

    @Autowired
    public EmailServiceImpl(
            JavaMailSender mailSender,
            @Qualifier("EmailConfig")
            EmailConfig config) {
        this.mailSender = mailSender;
        this.config = config;
    }

    @Override
    @Async
    public void send(String to, String subject,String message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
        helper.setText(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(config.getSendingEmail());
        mailSender.send(mimeMessage);
    }

}
