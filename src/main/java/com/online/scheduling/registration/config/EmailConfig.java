package com.online.scheduling.registration.config;

import com.online.scheduling.registration.implementations.services.EmailServiceImpl;
import com.online.scheduling.registration.implementations.services.EmailServiceImplBlank;
import com.online.scheduling.registration.interfaces.IEmailService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@Getter
@Qualifier("EmailConfig")
public class EmailConfig {
    @Value("${spring.mail.username}")
    private String sendingEmail;
    @Value("${registration.send-email}")
    private Boolean toSend;

    private final JavaMailSender javaMailSender;

    public EmailConfig(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Bean
    IEmailService emailService(){
        if(!toSend)
            return new EmailServiceImplBlank();
        EmailServiceImpl emailService = new EmailServiceImpl(javaMailSender);
        emailService.setDefaultFrom(sendingEmail);
        return emailService;
    }
}
