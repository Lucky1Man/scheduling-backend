package com.online.scheduling.schedule.cleardata.plannedaction;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.validators.plannedaction.PlannedActionValidator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.online.scheduling.schedule.enums.PlannedActionValidatorComponentsIds.PA_TIME_BORDERS_VALIDATOR_COMPONENT;
import static com.online.scheduling.schedule.enums.PlannedStuffRequestsPairedComponents.NAME_VALIDATION_VALIDATOR_CONFIG;
import static com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds.PS_ID_VALIDATOR_COMPONENT;

public class ClearUpdatingActionRequest {
    @Getter
    private final List<PlannedAction> actions;
    @Getter
    private List<PlannedActionContainer> actionContainersToSave;
    @Getter
    private final List<PlannedActionContainer> actionContainersToUpdate = new ArrayList<>();
    private final PlannedActionValidator actionValidator;

    public ClearUpdatingActionRequest(List<PlannedAction> actions) {
        this.actions = actions;
        this.actionValidator = SchedulingProvider.getValidator().getActionValidator();
        init();
    }
    public void init(){
        actionValidator.validateAllUsing(actions, new ArrayList<>());
        validateIgnoringNulls();
        actionContainersToSave = SchedulingProvider.getInitializer().getActionContainerInitializer().doInit(actionContainersToSave, actions);
    }

    private void validateIgnoringNulls(){
        for(var action : actions){
            if(action.getId() == null)
                throw new UnableToExecuteQueryException(String.format("Updating action {%s} can not have empty id", action));
            actionValidator.validateUsing(action, List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
            PlannedActionContainer plannedActionContainer = action.getPlannedActionContainer();
            if(plannedActionContainer != null && plannedActionContainer.getId() != null)
                actionContainersToUpdate.add(plannedActionContainer);
            if(action.getName() != null)
                actionValidator.validateUsing(action, NAME_VALIDATION_VALIDATOR_CONFIG.getComponentsIds());
            if(action.getEndsAt() != null)
                actionValidator.validateUsing(action, List.of(PA_TIME_BORDERS_VALIDATOR_COMPONENT.getId()));
        }
    }
}
