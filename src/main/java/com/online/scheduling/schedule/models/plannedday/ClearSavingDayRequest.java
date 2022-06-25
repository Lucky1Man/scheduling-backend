package com.online.scheduling.schedule.models.plannedday;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.validators.plannedday.PlannedDayValidator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ClearSavingDayRequest {
    @Getter
    private final List<PlannedDay> plannedDays;
    private final PlannedDayValidator dayValidator;
    @Getter
    private List<PlannedAction> plannedActionsToSave = new ArrayList<>();
    @Getter
    private final List<PlannedAction> plannedActionsToUpdate = new ArrayList<>();
    @Getter
    private List<PlannedDayContainer> dayContainersToSave = new ArrayList<>();
    @Getter
    private final List<PlannedDayContainer> dayContainersToUpdate = new ArrayList<>();

    public ClearSavingDayRequest(List<PlannedDay> plannedDays) {
        this.plannedDays = plannedDays;
        this.dayValidator = SchedulingProvider.getValidator().getDayValidator();
        init();
    }

    private void init(){
        dayValidator.validateAll(plannedDays);
        setContainersToUpdate();
        dayContainersToSave = SchedulingProvider.getInitializer().getDayContainerInitializer().doInit(dayContainersToSave,plannedDays);
        plannedActionsToSave = SchedulingProvider.getInitializer().getActionInitializer().doInit(plannedActionsToSave,plannedDays);
        setPlannedActionsToUpdate();
    }
    private void setContainersToUpdate() {
        for(var day : plannedDays){
            PlannedDayContainer plannedDayContainer = day.getPlannedDayContainer();
            if(plannedDayContainer != null && plannedDayContainer.getId() != null)
                dayContainersToUpdate.add(plannedDayContainer);
        }
    }

    private void setPlannedActionsToUpdate(){
        for(var day : plannedDays){
            for(var action : day.getPlannedActions()){
                if(action.getId() != null)
                    plannedActionsToUpdate.add(action);
            }
        }
    }
}
