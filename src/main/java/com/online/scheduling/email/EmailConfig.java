package com.online.scheduling.email;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Getter
@Qualifier("EmailConfig")
public class EmailConfig {
    @Value("${spring.mail.username}")
    private String sendingEmail;
}
