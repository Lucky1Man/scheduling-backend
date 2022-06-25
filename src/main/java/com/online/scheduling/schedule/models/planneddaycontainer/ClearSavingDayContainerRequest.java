package com.online.scheduling.schedule.models.planneddaycontainer;

import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.validators.planneddaycontainer.PlannedDayContainerValidator;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.models.generalized.ClearSavingPlannedStuffContainerRequestTemplate;
import lombok.Getter;

import java.util.List;

public class ClearSavingDayContainerRequest extends ClearSavingPlannedStuffContainerRequestTemplate<PlannedDayContainer> {
    public ClearSavingDayContainerRequest(List<PlannedDayContainer> plannedStuff) {
        super(plannedStuff);
    }

    @Override
    protected IPlannedStuffValidator<PlannedDayContainer> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getDayContainerValidator();
    }

}
