package com.online.scheduling.schedule.cleardata.shcedulecontainer;

import com.online.scheduling.schedule.cleardata.generalized.ClearDeletingAnythingPlannedRequestTemplate;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;

import java.util.List;
import java.util.stream.Collectors;

public class ClearDeletingScheduleContainerRequest extends ClearDeletingAnythingPlannedRequestTemplate<ScheduleContainer> {
    public ClearDeletingScheduleContainerRequest(List<Long> ids) {
        super(ids);
    }

    @Override
    protected List<ScheduleContainer> initPlannedStuff(List<Long> ids) {
        return ids.stream().map(id->ScheduleContainer.builder().id(id).build()).collect(Collectors.toList());
    }

    @Override
    protected IPlannedStuffValidator<ScheduleContainer> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getScheduleContainerValidator();
    }
}
