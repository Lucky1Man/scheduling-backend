package com.online.scheduling.schedule.models.plannedactioncontainer;

import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.models.generalized.ClearDeletingAnythingPlannedRequestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class ClearDeletingActionContainerRequest extends ClearDeletingAnythingPlannedRequestTemplate<PlannedActionContainer> {
    public ClearDeletingActionContainerRequest(List<Long> ids) {
        super(ids);
    }

    @Override
    protected List<PlannedActionContainer> initPlannedStuff(List<Long> ids) {
        return ids.stream().map(id->PlannedActionContainer.builder().id(id).build()).collect(Collectors.toList());
    }

    @Override
    protected IPlannedStuffValidator<PlannedActionContainer> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getActionContainerValidator();
    }
}
