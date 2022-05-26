package com.online.scheduling.registration.implementations.validators;

import com.online.scheduling.registration.models.RegistrationRequest;
import com.online.scheduling.exceptions.RegistrationInvalidDataException;
import com.online.scheduling.registration.interfaces.IRegistrationRequestValidatorComponent;
import lombok.Getter;

@Getter
public class RegRequestFirstNameValidatorCompImpl implements IRegistrationRequestValidatorComponent {
    private final String prefix = "First";
    @Override
    public boolean isValid(RegistrationRequest request){
        String firstName = request.getFirstName();
        if(firstName == null || firstName.isEmpty())
            throw new RegistrationInvalidDataException(String.format("%s name should not be empty", prefix));
        String nameRegex = ".*[a-zA-Z]+.*";
        if(!firstName.matches(nameRegex))
            throw new RegistrationInvalidDataException(String.format("%s name should include at least 1 letter", prefix));
        return true;
    }
}
