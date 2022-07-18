package com.online.scheduling.schedule.cleardata.generalized;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import lombok.Getter;

import java.util.List;

import static com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds.PS_ID_VALIDATOR_COMPONENT;

public abstract class ClearDeletingAnythingPlannedRequestTemplate<T extends IPlannedStuff<T>> {
    @Getter
    private final List<Long> ids;
    private final List<T> plannedStuff;
    private final IPlannedStuffValidator<T> stuffValidator;

    public ClearDeletingAnythingPlannedRequestTemplate(List<Long> ids) {
        this.ids = ids;
        this.plannedStuff = initPlannedStuff(ids);
        this.stuffValidator = initPlannedStuffValidator();
        init();
    }

    protected abstract List<T> initPlannedStuff(List<Long> ids);
    protected abstract IPlannedStuffValidator<T> initPlannedStuffValidator();

    protected void init(){
        stuffValidator.validateAllUsing(plannedStuff, List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
    }
}
