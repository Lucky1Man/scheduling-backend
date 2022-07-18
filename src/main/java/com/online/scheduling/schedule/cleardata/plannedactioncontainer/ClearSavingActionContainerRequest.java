package com.online.scheduling.schedule.cleardata.plannedactioncontainer;

import com.online.scheduling.schedule.cleardata.generalized.ClearSavingPlannedStuffContainerRequestTemplate;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;

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
