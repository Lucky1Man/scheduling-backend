package com.online.scheduling.schedule.models.schedule;

import com.online.scheduling.schedule.entities.*;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.validators.schedule.ScheduleValidator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ClearSavingScheduleRequest {
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

    public ClearSavingScheduleRequest(List<Schedule> schedules) {
        this.schedules = schedules;
        this.scheduleValidator = SchedulingProvider.getValidator().getScheduleValidator();
        init();
    }

    private void init(){
        scheduleValidator.validateAll(schedules);
        setContainersToUpdate();
        scheduleContainersToSave = SchedulingProvider.getInitializer().getScheduleContainerInitializer().doInit(scheduleContainersToSave,schedules);
        plannedDaysToSave = SchedulingProvider.getInitializer().getDayInitializer().doInit(plannedDaysToSave,schedules);
        setPlannedDaysToUpdate();
    }

    private void setContainersToUpdate() {
        for(var schedule : schedules){
            ScheduleContainer scheduleContainer = schedule.getScheduleContainer();
            if(scheduleContainer != null && scheduleContainer.getId() != null)
                scheduleContainersToUpdate.add(scheduleContainer);
        }
    }

    private void setPlannedDaysToUpdate(){
        for(var schedule : schedules){
            for(var day : schedule.getDays()){
                if(day.getId() != null)
                    plannedDaysToUpdate.add(day);
            }
        }
    }
}
