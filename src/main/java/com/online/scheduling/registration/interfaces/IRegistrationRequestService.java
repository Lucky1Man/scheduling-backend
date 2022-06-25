package com.online.scheduling.registration.interfaces;

import com.online.scheduling.registration.models.RegistrationRequest;
import com.online.scheduling.exceptions.RegistrationInvalidDataException;

public interface IRegistrationRequestService {
    void register(RegistrationRequest request) throws RegistrationInvalidDataException;

    void confirmToken(String token);
}
