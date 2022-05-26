package com.online.scheduling.registration.implementations;

import com.online.scheduling.exceptions.EmailAlreadyRegisteredException;
import com.online.scheduling.registration.models.RegistrationRequest;
import com.online.scheduling.exceptions.RegistrationInvalidDataException;
import com.online.scheduling.registration.interfaces.IRegistrationRequestValidatorComponent;
import com.online.scheduling.registration.interfaces.IRegistrationRequestValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service("RegistrationValidator")
public class RegistrationRequestValidatorImpl implements IRegistrationRequestValidator {
    @Override
    public boolean validateAll(RegistrationRequest request,
                               Collection<IRegistrationRequestValidatorComponent> component)
            throws RegistrationInvalidDataException, EmailAlreadyRegisteredException {
        component.forEach(comp -> comp.isValid(request));
        return true;
    }
}
