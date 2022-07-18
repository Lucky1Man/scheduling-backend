package com.online.scheduling.schedule.cleardata.plannedday;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
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
    private final List<PlannedDay> plannedDays;
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

    private void init() {
        //searching for duplicates
        dayValidator.validateAllUsing(plannedDays, new ArrayList<>());
        validateAllIgnoringNulls();
        plannedActionsToSave = SchedulingProvider.getInitializer().getActionInitializer().doInit(plannedActionsToSave, plannedDays);
        plannedDayContainersToSave = SchedulingProvider.getInitializer().getDayContainerInitializer().doInit(plannedDayContainersToSave, plannedDays);
    }

    private void validateAllIgnoringNulls() {
        for (var day : plannedDays) {
            Long dayId = day.getId();
            if (dayId == null)
                throw new UnableToExecuteQueryException(String.format("Updating day {%s} can not have empty id", day));
            dayValidator.validateUsing(day, List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
            if (day.getName() != null)
                dayValidator.validateUsing(day, NAME_VALIDATION_VALIDATOR_CONFIG.getComponentsIds());
            for (var action : day.getPlannedActions()) {
                Long actionId = action.getId();
                if (actionId != null && actionId > 0)
                    plannedActionsToUpdate.add(action);
            }
            PlannedDayContainer plannedDayContainer = day.getPlannedDayContainer();
            if (plannedDayContainer != null && plannedDayContainer.getId() != null)
                plannedDayContainersToUpdate.add(plannedDayContainer);
        }
    }
}
