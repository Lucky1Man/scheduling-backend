package com.online.scheduling.registration.controllers;

import com.online.scheduling.exceptions.InvalidConfirmationTokenException;
import com.online.scheduling.exceptions.RegistrationInvalidDataException;
import com.online.scheduling.exceptions.UnableEmailException;
import com.online.scheduling.registration.interfaces.IRegistrationRequestService;
import com.online.scheduling.exceptions.EmailAlreadyRegisteredException;
import com.online.scheduling.registration.models.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {
    private final IRegistrationRequestService registrationRequestService;
    @Autowired
    public RegistrationController(
            @Qualifier("RegistrationRequestService")
            IRegistrationRequestService registrationRequestService) {
        this.registrationRequestService = registrationRequestService;
    }

    @PostMapping
    public void register(@RequestBody RegistrationRequest request)
            throws
            RegistrationInvalidDataException,
            EmailAlreadyRegisteredException,
            UnableEmailException {
        registrationRequestService.register(request);
    }
    @GetMapping("confirm")
    public void confirm(@RequestParam("token") String token)
            throws
            InvalidConfirmationTokenException {
        registrationRequestService.confirmToken(token);
    }
}
