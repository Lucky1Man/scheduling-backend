package com.online.scheduling.user.implementations;

import com.online.scheduling.user.entities.User;
import com.online.scheduling.exceptions.EmailAlreadyRegisteredException;
import com.online.scheduling.user.interfaces.IUserValidator;
import com.online.scheduling.user.interfaces.IUserValidatorComponent;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service("UserValidator")
public class UserValidatorImpl implements IUserValidator {
    @Override
    public boolean validateAll(User user,
                               Collection<IUserValidatorComponent> validators) throws EmailAlreadyRegisteredException {
        validators.forEach(validator -> validator.isValid(user));
        return true;
    }
}
