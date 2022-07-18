package com.online.scheduling.registration.interfaces;

import com.online.scheduling.registration.models.RegistrationRequest;

public interface IRegistrationRequestValidatorComponent {
    boolean isValid(RegistrationRequest request);
}
