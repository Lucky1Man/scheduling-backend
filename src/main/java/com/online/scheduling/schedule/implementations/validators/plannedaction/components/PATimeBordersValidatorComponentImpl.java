package com.online.scheduling.schedule.implementations.validators.plannedaction.components;

import com.online.scheduling.exceptions.ValidationExceptionGivenTimeException;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class PATimeBordersValidatorComponentImpl implements IPlannedStuffValidatorComponent<PlannedAction> {

    @Override
    public boolean isValid(PlannedAction validated) {//TODO make it better
        LocalTime startsAt = validated.getStartsAt();
        if(startsAt == null)
            throw new ValidationExceptionGivenTimeException(String.format("Planned action {%s} do not have Starts at value", validated));
        LocalTime endsAt = validated.getEndsAt();
        if(endsAt == null)
            throw new ValidationExceptionGivenTimeException(String.format("Planned action {%s} do not have Ends at value", validated));
        if(endsAt.isBefore(startsAt))
            throw new ValidationExceptionGivenTimeException(String.format(
                    "Error in planned action with name %s ends at is earlier that starts at", validated.getName()));
        return true;
    }
}
