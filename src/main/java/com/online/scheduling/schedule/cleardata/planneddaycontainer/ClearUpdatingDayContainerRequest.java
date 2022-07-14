package com.online.scheduling.schedule.cleardata.planneddaycontainer;

import com.online.scheduling.schedule.cleardata.generalized.ClearUpdatingPlannedStuffContainerRequestTemplate;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;

import java.util.List;

public class ClearUpdatingDayContainerRequest extends ClearUpdatingPlannedStuffContainerRequestTemplate<PlannedDayContainer> {
    public ClearUpdatingDayContainerRequest(List<PlannedDayContainer> plannedDayContainers) {
        super(plannedDayContainers,"day container");
    }

    @Override
    protected IPlannedStuffValidator<PlannedDayContainer> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getDayContainerValidator();
    }
}
