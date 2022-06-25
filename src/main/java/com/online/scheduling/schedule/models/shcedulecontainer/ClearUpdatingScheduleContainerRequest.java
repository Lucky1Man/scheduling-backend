package com.online.scheduling.schedule.models.shcedulecontainer;

import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.models.generalized.ClearUpdatingPlannedStuffContainerRequestTemplate;

import java.util.List;

public class ClearUpdatingScheduleContainerRequest extends ClearUpdatingPlannedStuffContainerRequestTemplate<ScheduleContainer> {
    public ClearUpdatingScheduleContainerRequest(List<ScheduleContainer> scheduleContainers) {
        super(scheduleContainers,"schedule container");
    }

    @Override
    protected IPlannedStuffValidator<ScheduleContainer> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getScheduleContainerValidator();
    }
}
