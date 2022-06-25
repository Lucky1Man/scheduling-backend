package com.online.scheduling.schedule.models.schedule;

import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.models.generalized.ClearDeletingAnythingPlannedRequestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class ClearDeletingScheduleRequest extends ClearDeletingAnythingPlannedRequestTemplate<Schedule> {
    public ClearDeletingScheduleRequest(List<Long> ids) {
        super(ids);
    }

    @Override
    protected List<Schedule> initPlannedStuff(List<Long> ids) {
        return ids.stream().map(id-> Schedule.builder().id(id).build()).collect(Collectors.toList());
    }

    @Override
    protected IPlannedStuffValidator<Schedule> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getScheduleValidator();
    }
}
