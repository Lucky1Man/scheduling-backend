package com.online.scheduling.schedule.models.plannedday;

import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.models.generalized.ClearDeletingAnythingPlannedRequestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class ClearDeletingDayRequest extends ClearDeletingAnythingPlannedRequestTemplate<PlannedDay> {
    public ClearDeletingDayRequest(List<Long> ids) {
        super(ids);
    }

    @Override
    protected List<PlannedDay> initPlannedStuff(List<Long> ids) {
        return ids.stream().map(id->PlannedDay.builder().id(id).build()).collect(Collectors.toList());
    }

    @Override
    protected IPlannedStuffValidator<PlannedDay> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getDayValidator();
    }
}
