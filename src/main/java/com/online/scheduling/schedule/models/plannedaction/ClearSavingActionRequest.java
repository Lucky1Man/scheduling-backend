package com.online.scheduling.schedule.models.plannedaction;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.validators.plannedaction.PlannedActionValidator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ClearSavingActionRequest {

    @Getter
    private final List<PlannedAction> plannedActions;
    private final PlannedActionValidator actionValidator;
    @Getter
    private final List<PlannedActionContainer> actionContainersToSave;
    @Getter
    private final List<PlannedActionContainer> actionContainersToUpdate = new ArrayList<>();

    public ClearSavingActionRequest(List<PlannedAction> actions) {
        this.plannedActions = actions;
        this.actionValidator = SchedulingProvider.getValidator().getActionValidator();
        init();
        this.actionContainersToSave = SchedulingProvider.getInitializer().getActionContainerInitializer().doInit(new ArrayList<>(),plannedActions);
    }

    private void init(){
        actionValidator.validateAll(plannedActions);
        getContainersToUpdate();
    }

    private void getContainersToUpdate() {
        for(var action : plannedActions){
            PlannedActionContainer plannedActionContainer = action.getPlannedActionContainer();
            if(plannedActionContainer != null && plannedActionContainer.getId() != null)
                actionContainersToUpdate.add(plannedActionContainer);
        }
    }
}