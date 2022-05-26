package com.online.scheduling.user.implementations;

import com.online.scheduling.user.interfaces.IUserValidatorComponent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;

@Configuration
@Qualifier("UserConfig")
public class UserConfig {
    public Collection<IUserValidatorComponent> getValidators() {
        return List.of(
        );
    }
}
