package com.online.scheduling.schedule.implementations.validators.schedule.components;

import com.online.scheduling.exceptions.UserNotConfirmedException;
import com.online.scheduling.exceptions.ValidationExceptionScheduleOwner;
import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.user.entities.User;
import com.online.scheduling.user.implementations.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class SOwnerValidatorImpl implements IPlannedStuffValidatorComponent<Schedule> {
    private final UserService userService;

    public SOwnerValidatorImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(Schedule validated) {
        User owner = validated.getOwner();
        if(owner != null){
            if(!owner.isEnabled()){
                throw new UserNotConfirmedException(String.format("User %s not confirmed", owner));
            }
            String email = owner.getEmail();
            userService.getUserByEmail(email).orElseThrow(
                    () -> new ValidationExceptionScheduleOwner(
                            String.format("User with email %s not found", email))
            );
        }

        return true;
    }
}
