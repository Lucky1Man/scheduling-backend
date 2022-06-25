package com.online.scheduling.schedule.implementations.validators;

import com.online.scheduling.schedule.implementations.validators.plannedaction.PlannedActionValidator;
import com.online.scheduling.schedule.implementations.validators.plannedactioncontainer.PlannedActionContainerValidator;
import com.online.scheduling.schedule.implementations.validators.plannedday.PlannedDayValidator;
import com.online.scheduling.schedule.implementations.validators.planneddaycontainer.PlannedDayContainerValidator;
import com.online.scheduling.schedule.implementations.validators.schedule.ScheduleValidator;
import com.online.scheduling.schedule.implementations.validators.schedulecontainer.ScheduleContainerValidator;
import lombok.Getter;
import org.springframework.stereotype.Component;



@Component
@Getter
public class ValidatorsHolder {
    private final PlannedActionContainerValidator actionContainerValidator;
    private final PlannedActionValidator actionValidator;
    private final PlannedDayContainerValidator dayContainerValidator;
    private final PlannedDayValidator dayValidator;
    private final ScheduleContainerValidator scheduleContainerValidator;
    private final ScheduleValidator scheduleValidator;

    public ValidatorsHolder(
            PlannedActionContainerValidator actionContainerValidator,
            PlannedActionValidator actionValidator,
            PlannedDayContainerValidator dayContainerValidator,
            PlannedDayValidator dayValidator,
            ScheduleContainerValidator scheduleContainerValidator,
            ScheduleValidator scheduleValidator) {
        this.actionContainerValidator = actionContainerValidator;
        this.actionValidator = actionValidator;
        this.dayContainerValidator = dayContainerValidator;
        this.dayValidator = dayValidator;
        this.scheduleContainerValidator = scheduleContainerValidator;
        this.scheduleValidator = scheduleValidator;
    }
}
