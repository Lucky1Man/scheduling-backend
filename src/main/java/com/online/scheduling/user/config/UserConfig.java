package com.online.scheduling.user.config;

import com.online.scheduling.user.entities.User;
import com.online.scheduling.user.interfaces.IUserValidatorComponent;
import com.online.scheduling.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Configuration
@Qualifier("UserConfig")
public class UserConfig {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public Collection<IUserValidatorComponent> getValidators() {
        return List.of(
        );
    }
    
    @PostConstruct
    private void setUpDefaultUser(){
        Optional<User> test = userRepository.findByEmail("test");
        if(test.isEmpty())
            userRepository.save(User.builder().email("test").firstName("test").lastName("test").locked(false).password(passwordEncoder.encode("1")).enabled(true).build());
    }
}

