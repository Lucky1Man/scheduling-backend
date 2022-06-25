package com.online.scheduling.schedule.models.planneddaycontainer;

import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.models.generalized.ClearDeletingAnythingPlannedRequestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class ClearDeletingDayContainerRequest extends ClearDeletingAnythingPlannedRequestTemplate<PlannedDayContainer> {
    public ClearDeletingDayContainerRequest(List<Long> ids) {
        super(ids);
    }

    @Override
    protected List<PlannedDayContainer> initPlannedStuff(List<Long> ids) {
        return ids.stream().map(id->PlannedDayContainer.builder().id(id).build()).collect(Collectors.toList());
    }

    @Override
    protected IPlannedStuffValidator<PlannedDayContainer> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getDayContainerValidator();
    }
}
