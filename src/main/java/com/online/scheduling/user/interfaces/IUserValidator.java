package com.online.scheduling.user.interfaces;

import com.online.scheduling.user.User;
import com.online.scheduling.exceptions.EmailAlreadyRegisteredException;

import java.util.Collection;

public interface IUserValidator {
    boolean validateAll(User user, Collection<IUserValidatorComponent> validators) throws EmailAlreadyRegisteredException;
}
