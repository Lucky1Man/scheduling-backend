package com.online.scheduling.schedule.implementations.validators.generalized.components;

import com.online.scheduling.exceptions.ValidationExceptionGivenBadName;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import lombok.Getter;
import lombok.Setter;


public class PSNameValidatorComponentImpl<T extends IPlannedStuff<T>>{
    @Setter
    @Getter
    private String prefix;
    public boolean isValid(T validated) {
        String name = validated.getName();
        if(name == null || name.isEmpty() || name.isBlank())
            throw new ValidationExceptionGivenBadName(String.format("%s name should not be empty", prefix));
        String nameRegex = ".*[a-zA-Z]+.*";
        if(!name.matches(nameRegex))
            throw new ValidationExceptionGivenBadName(String.format("%s name should include at least 1 letter", prefix));
        return true;
    }
}
