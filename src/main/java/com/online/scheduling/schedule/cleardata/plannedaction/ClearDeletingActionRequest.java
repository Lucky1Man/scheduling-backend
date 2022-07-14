package com.online.scheduling.schedule.cleardata.plannedaction;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import com.online.scheduling.schedule.cleardata.generalized.ClearDeletingAnythingPlannedRequestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class ClearDeletingActionRequest extends ClearDeletingAnythingPlannedRequestTemplate<PlannedAction> {
    public ClearDeletingActionRequest(List<Long> ids) {
        super(ids);
    }

    @Override
    protected List<PlannedAction> initPlannedStuff(List<Long> ids) {
        return ids.stream().map(id->PlannedAction.builder().id(id).build()).collect(Collectors.toList());
    }

    @Override
    protected IPlannedStuffValidator<PlannedAction> initPlannedStuffValidator() {
        return SchedulingProvider.getValidator().getActionValidator();
    }

//    @Getter
//    private final List<Long> ids;
//    private final List<PlannedAction> plannedActions;
//    private final PlannedActionValidator actionValidator;
//
//    public ClearDeletingActionRequest(List<Long> ids) {
//        this.ids = ids;
//        this.plannedActions = ids.stream().map(id->PlannedAction.builder().id(id).build()).collect(Collectors.toList());
//        this.actionValidator = SchedulingProvider.getValidator().getActionValidator();
//        init();
//    }
//
//    private void init(){
//        actionValidator.validateAllUsing(plannedActions, List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
//    }
}
