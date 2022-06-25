package com.online.scheduling.schedule.models.shcedulecontainer;

import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.models.generalized.ClearSavingPlannedStuffContainerRequestTemplate;

import java.util.List;

public class ClearSavingScheduleContainerRequest extends ClearSavingPlannedStuffContainerRequestTemplate<ScheduleContainer> {
    public ClearSavingScheduleContainerRequest(List<ScheduleContainer> plannedStuff) {
        super(plannedStuff);
    }

    @Override
    protected IPlannedStuffValidator<ScheduleContainer> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getScheduleContainerValidator();
    }
}
