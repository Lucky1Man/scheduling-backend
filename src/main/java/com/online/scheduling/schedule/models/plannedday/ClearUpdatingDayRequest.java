package com.online.scheduling.schedule.models.plannedday;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.carriedoutfunctions.CheckIfUpdatingStuffNeedsToBeAddedToItsOwner;
import com.online.scheduling.schedule.carriedoutfunctions.RemoveMarkedPlannedStuffFromItsOwner;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.validators.plannedday.PlannedDayValidator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.online.scheduling.schedule.enums.PlannedStuffRequestsPairedComponents.NAME_VALIDATION_VALIDATOR_CONFIG;
import static com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds.PS_ID_VALIDATOR_COMPONENT;

public class ClearUpdatingDayRequest {
    @Getter
    private List<PlannedDay> plannedDays;
    @Getter
    private List<List<PlannedAction>> idsOfPlannedActionsToBeRemovedFromDays;
    @Getter
    private List<PlannedAction> plannedActionsToSave = new ArrayList<>();
    @Getter
    private final List<PlannedAction> plannedActionsToUpdate = new ArrayList<>();
    @Getter
    private List<PlannedDayContainer> plannedDayContainersToSave = new ArrayList<>();
    @Getter
    private final List<PlannedDayContainer> plannedDayContainersToUpdate = new ArrayList<>();
    private final PlannedDayValidator dayValidator;

    public ClearUpdatingDayRequest(List<PlannedDay> plannedDays) {
        this.plannedDays = plannedDays;
        this.dayValidator = SchedulingProvider.getValidator().getDayValidator();
        init();
    }

    private void init(){
        //#1 initializes idsOfPlannedActionsToBeRemovedFromDays
        initIdsOfPlannedActionsToBeRemovedFromDays();
        //#2 fills idsOfDaysToBeRemovedFromSchedules with stuff to be removed
        // NOTE: validateAllIgnoringNulls must be called before #3
        validateAllIgnoringNulls();
        //#3 removes all marked stuff from its owner depending on witten things to idsOfDaysToBeRemovedFromSchedules
        plannedDays = RemoveMarkedPlannedStuffFromItsOwner.removeMarkedPlannedStuffFromItsOwner(plannedDays, idsOfPlannedActionsToBeRemovedFromDays);

        plannedActionsToSave = SchedulingProvider.getInitializer().getActionInitializer().doInit(plannedActionsToSave,plannedDays);
        plannedDayContainersToSave = SchedulingProvider.getInitializer().getDayContainerInitializer().doInit(plannedDayContainersToSave,plannedDays);
    }
    public List<PlannedAction> getActionsToBeAdded(){
        ArrayList<PlannedAction> plannedActions = new ArrayList<>(plannedActionsToSave);
        plannedActions.addAll(
                CheckIfUpdatingStuffNeedsToBeAddedToItsOwner.getUpdatingPlannedStuffToBeAdded(
                        plannedActionsToUpdate,
                        plannedDays,
                        SchedulingProvider.getRepositories().getDayRepository()));
        return plannedActions;
    }


    private void removeMarkedPlannedActionsFromPlannedDays() {
        int i = 0;
        for(var day : plannedDays){
            List<PlannedAction> plannedActionsToRemove = idsOfPlannedActionsToBeRemovedFromDays.get(i);
            for(int j = 0; j < day.getPlannedActions().size(); j++){
                var actions = day.getPlannedActions().get(j);
                for(var actionToRem : plannedActionsToRemove){
                    if(actions.getId().equals(actionToRem.getId()))
                        day.getPlannedActions().remove(j);
                }
            }
            i++;
        }
    }

    private void initIdsOfPlannedActionsToBeRemovedFromDays(){
        idsOfPlannedActionsToBeRemovedFromDays = new ArrayList<>();
        for(var day : plannedDays){
            idsOfPlannedActionsToBeRemovedFromDays.add(new ArrayList<>());
        }
    }
    private void validateAllIgnoringNulls(){
        int i = 0;
        for(var day : plannedDays){
            Long dayId = day.getId();
            if(dayId == null)
                throw new UnableToExecuteQueryException(String.format("Updating day {%s} can not have empty id", day));
            dayValidator.validateUsing(day, List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
            if(day.getName() != null)
                dayValidator.validateUsing(day, NAME_VALIDATION_VALIDATOR_CONFIG.getComponentsIds());
            List<PlannedAction> plannedActions = day.getPlannedActions();
            if(plannedActions != null && !plannedActions.isEmpty()){
                for(var action : plannedActions){
                    Long actionId = action.getId();
                    if(actionId != null && -actionId > 0){
                        action.setId(-actionId);
                        idsOfPlannedActionsToBeRemovedFromDays.get(i).add(action);
                        continue;
                    }
                    if(actionId != null)
                        plannedActionsToUpdate.add(action);
                }
            }
            i++;
            PlannedDayContainer plannedDayContainer = day.getPlannedDayContainer();
            if(plannedDayContainer != null && plannedDayContainer.getId() != null)
                plannedDayContainersToUpdate.add(plannedDayContainer);
        }
    }
}
