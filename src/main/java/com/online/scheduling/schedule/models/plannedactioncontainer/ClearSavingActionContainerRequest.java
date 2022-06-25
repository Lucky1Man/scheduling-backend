package com.online.scheduling.schedule.models.plannedactioncontainer;

import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.validators.plannedactioncontainer.PlannedActionContainerValidator;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.models.generalized.ClearDeletingAnythingPlannedRequestTemplate;
import com.online.scheduling.schedule.models.generalized.ClearSavingPlannedStuffContainerRequestTemplate;
import lombok.Getter;

import java.util.List;

public class ClearSavingActionContainerRequest extends ClearSavingPlannedStuffContainerRequestTemplate<PlannedActionContainer> {

    public ClearSavingActionContainerRequest(List<PlannedActionContainer> plannedStuff) {
        super(plannedStuff);
    }

    @Override
    protected IPlannedStuffValidator<PlannedActionContainer> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getActionContainerValidator();
    }
//    @Getter
//    private final List<PlannedActionContainer> actionContainers;
//    private final PlannedActionContainerValidator actionContainerValidator;
//
//    public ClearSavingActionContainerRequest(
//            List<PlannedActionContainer> actionContainers) {
//        this.actionContainers = actionContainers;
//        this.actionContainerValidator = SchedulingProvider.getValidator().getActionContainerValidator();
//        init();
//    }
//
//    private void init(){
//        actionContainerValidator.validateAll(actionContainers);
//    }
}
