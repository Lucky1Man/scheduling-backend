package com.online.scheduling.schedule.models.planneddaycontainer;

import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.models.generalized.ClearUpdatingPlannedStuffContainerRequestTemplate;

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
