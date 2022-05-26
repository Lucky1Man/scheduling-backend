package com.online.scheduling.registration.implementations.validators;

import com.online.scheduling.exceptions.EmailAlreadyRegisteredException;
import com.online.scheduling.registration.models.RegistrationRequest;
import com.online.scheduling.exceptions.RegistrationInvalidDataException;
import com.online.scheduling.registration.interfaces.IRegistrationRequestValidatorComponent;
import com.online.scheduling.user.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

public class RegRequestEmailValidatorCompImpl implements IRegistrationRequestValidatorComponent {
    private final UserService userService;

    @Autowired
    public RegRequestEmailValidatorCompImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(RegistrationRequest request){
        String email = request.getEmail();
        if(email == null || email.isEmpty())
            throw new RegistrationInvalidDataException(String.format("Email %s should not be empty", email));
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if(!pattern.matcher(email).matches())
            throw new RegistrationInvalidDataException(String.format("Email %s is invalid", email));
        if(userService.getUserByEmail(email).isPresent())
            throw new EmailAlreadyRegisteredException(String.format("Email %s is already registered", email));
        return true;
    }
}
