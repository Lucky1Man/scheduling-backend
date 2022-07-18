package com.online.scheduling.schedule.cleardata.shcedulecontainer;

import com.online.scheduling.schedule.cleardata.generalized.ClearSavingPlannedStuffContainerRequestTemplate;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;

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
