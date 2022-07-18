package com.online.scheduling.schedule.cleardata.planneddaycontainer;

import com.online.scheduling.schedule.cleardata.generalized.ClearSavingPlannedStuffContainerRequestTemplate;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;

import java.util.List;

public class ClearSavingDayContainerRequest extends ClearSavingPlannedStuffContainerRequestTemplate<PlannedDayContainer> {
    public ClearSavingDayContainerRequest(List<PlannedDayContainer> plannedStuff) {
        super(plannedStuff,"planned day container");
    }

    @Override
    protected IPlannedStuffValidator<PlannedDayContainer> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getDayContainerValidator();
    }

}
