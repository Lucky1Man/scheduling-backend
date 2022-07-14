package com.online.scheduling.schedule.cleardata.shcedulecontainer;

import com.online.scheduling.schedule.cleardata.generalized.ClearUpdatingPlannedStuffContainerRequestTemplate;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;

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
