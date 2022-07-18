package com.online.scheduling.schedule.cleardata.schedule;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.validators.schedule.ScheduleValidator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.online.scheduling.schedule.enums.PlannedStuffRequestsPairedComponents.NAME_VALIDATION_VALIDATOR_CONFIG;
import static com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds.PS_ID_VALIDATOR_COMPONENT;

public class ClearUpdatingScheduleRequest {
    @Getter
    private final List<Schedule> schedules;
    private final ScheduleValidator scheduleValidator;
    @Getter
    private List<PlannedDay> plannedDaysToSave = new ArrayList<>();
    @Getter
    private final List<PlannedDay> plannedDaysToUpdate = new ArrayList<>();
    @Getter
    private List<ScheduleContainer> scheduleContainersToSave = new ArrayList<>();
    @Getter
    private final List<ScheduleContainer> scheduleContainersToUpdate = new ArrayList<>();

    public ClearUpdatingScheduleRequest(List<Schedule> schedules) {
        this.schedules = schedules;
        scheduleValidator = SchedulingProvider.getValidator().getScheduleValidator();
        init();
    }

    private void init() {
        //searching for duplicates
        scheduleValidator.validateAllUsing(schedules, new ArrayList<>());
        validateAllIgnoringNulls();
        plannedDaysToSave = SchedulingProvider.getInitializer().getDayInitializer().doInit(plannedDaysToSave, schedules);
        scheduleContainersToSave = SchedulingProvider.getInitializer().getScheduleContainerInitializer().doInit(scheduleContainersToSave, schedules);
    }

    private void validateAllIgnoringNulls() {
        for (var schedule : schedules) {
            Long scheduleId = schedule.getId();
            if (scheduleId == null)
                throw new UnableToExecuteQueryException(String.format("Updating schedule {%s} can not have empty id", schedule));
            scheduleValidator.validateUsing(schedule, List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
            if (schedule.getName() != null)
                scheduleValidator.validateUsing(schedule, NAME_VALIDATION_VALIDATOR_CONFIG.getComponentsIds());
            for (var day : schedule.getDays()) {
                Long dayId = day.getId();
                if (dayId != null && dayId > 0)
                    plannedDaysToUpdate.add(day);
            }
            var scheduleContainer = schedule.getScheduleContainer();
            if (scheduleContainer != null && scheduleContainer.getId() != null)
                scheduleContainersToUpdate.add(scheduleContainer);
//            User owner = schedule.getOwner();
//            if(owner != null)
//                scheduleValidator.validateUsing(schedule, List.of(S_OWNER_VALIDATOR_COMPONENT.getId()));
        }
    }
}
