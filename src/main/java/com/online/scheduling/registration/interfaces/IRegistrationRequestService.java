package com.online.scheduling.registration.interfaces;

import com.online.scheduling.registration.models.RegistrationRequest;
import com.online.scheduling.exceptions.RegistrationInvalidDataException;
import com.online.scheduling.user.entities.User;

public interface IRegistrationRequestService {
    User register(RegistrationRequest request) throws RegistrationInvalidDataException;

    void confirmToken(String token);
}
