package com.online.scheduling.user.config;

import com.online.scheduling.user.interfaces.IUserValidatorComponent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;

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

