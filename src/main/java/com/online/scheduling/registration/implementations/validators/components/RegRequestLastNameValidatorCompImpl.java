package com.online.scheduling.registration.implementations.validators.components;

import com.online.scheduling.registration.models.RegistrationRequest;
import com.online.scheduling.exceptions.RegistrationInvalidDataException;
import com.online.scheduling.registration.interfaces.IRegistrationRequestValidatorComponent;
import lombok.Getter;

@Getter
public class RegRequestLastNameValidatorCompImpl implements IRegistrationRequestValidatorComponent {
    private final String prefix = "Last";
    @Override
    public boolean isValid(RegistrationRequest request){
        String lastName = request.getLastName();
        if(lastName == null || lastName.isEmpty())
            throw new RegistrationInvalidDataException(String.format("%s name should not be empty", prefix));
        String nameRegex = ".*[a-zA-Z]+.*";
        if(!lastName.matches(nameRegex))
            throw new RegistrationInvalidDataException(String.format("%s name should include at least 1 letter", prefix));
        return true;
    }
}
