package com.online.scheduling.schedule.models.generalized;


import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import lombok.Getter;

import java.util.List;

public abstract class ClearSavingPlannedStuffContainerRequestTemplate <T extends IPlannedStuff<T>> {
    @Getter
    private final List<T> plannedStuffContainers;
    private final IPlannedStuffValidator<T> stuffValidator;

    public ClearSavingPlannedStuffContainerRequestTemplate(List<T> plannedStuffContainers) {
        this.plannedStuffContainers = plannedStuffContainers;
        this.stuffValidator = initPlannedStuffValidator();
        init();
    }
    protected abstract IPlannedStuffValidator<T> initPlannedStuffValidator();
    protected void init(){
        stuffValidator.validateAll(plannedStuffContainers);
    }
}
