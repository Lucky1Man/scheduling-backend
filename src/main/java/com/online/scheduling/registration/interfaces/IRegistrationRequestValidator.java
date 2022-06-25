package com.online.scheduling.registration.interfaces;

import com.online.scheduling.registration.models.RegistrationRequest;

import java.util.Collection;

public interface IRegistrationRequestValidator {
    boolean validateAll(RegistrationRequest request,
                        Collection<IRegistrationRequestValidatorComponent> component);
}
