package com.online.scheduling.registration.config;

import com.online.scheduling.registration.interfaces.IRegistrationRequestValidatorComponent;
import com.online.scheduling.registration.implementations.validators.components.RegRequestEmailValidatorCompImpl;
import com.online.scheduling.registration.implementations.validators.components.RegRequestFirstNameValidatorCompImpl;
import com.online.scheduling.registration.implementations.validators.components.RegRequestLastNameValidatorCompImpl;
import com.online.scheduling.user.implementations.services.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;

@Configuration
@Qualifier("RegistrationConfig")
@Getter
public class RegistrationRequestConfig {

    private final UserService userService;
    @Value("${registration.send-email}")
    private Boolean sendEmail;
    @Value("${registration.confirmation-link}")
    private String confirmationLink;
    @Value("${registration.mail-subject}")
    private String confirmationEmailSubject;
    @Value("${registration.confirmation-token-expiration-minutes}")
    private Long confirmationTokenExpirationTime;


    @Autowired
    public RegistrationRequestConfig(
            @Qualifier("UserService")
            UserService userService) {
        this.userService = userService;
    }
    public Collection<IRegistrationRequestValidatorComponent> getValidators() {
        return List.of(
                regReqEmailValidator(),
                regReqFirstNameValidator(),
                regReqLastNameValidator()
        );
    }
    @Bean
    RegRequestFirstNameValidatorCompImpl regReqFirstNameValidator(){
        return new RegRequestFirstNameValidatorCompImpl();
    }
    @Bean
    RegRequestLastNameValidatorCompImpl regReqLastNameValidator(){
        return new RegRequestLastNameValidatorCompImpl();
    }
    @Bean
    RegRequestEmailValidatorCompImpl regReqEmailValidator(){
        return new RegRequestEmailValidatorCompImpl(userService);
    }
}
