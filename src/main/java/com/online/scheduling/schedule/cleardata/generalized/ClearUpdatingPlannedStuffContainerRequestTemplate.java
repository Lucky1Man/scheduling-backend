package com.online.scheduling.schedule.cleardata.generalized;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidator;
import lombok.Getter;

import java.util.List;

import static com.online.scheduling.schedule.enums.PlannedStuffRequestsPairedComponents.NAME_VALIDATION_VALIDATOR_CONFIG;
import static com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds.PS_ID_VALIDATOR_COMPONENT;
import static com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds.PS_USER_ACCOUNT_VALIDATOR_COMPONENT;

public abstract class ClearUpdatingPlannedStuffContainerRequestTemplate <T extends IPlannedStuff<T>> {
    @Getter
    private final List<T> plannedStuffContainers;
    private final IPlannedStuffValidator<T> stuffValidator;
    private final String stuffName;

    public ClearUpdatingPlannedStuffContainerRequestTemplate(
            List<T> plannedStuffContainers,
            String stuffName) {
        this.plannedStuffContainers = plannedStuffContainers;
        this.stuffValidator = initPlannedStuffValidator();
        this.stuffName = stuffName;
        init();
    }
    private void init(){
        stuffValidator.validateAllUsing(plannedStuffContainers, List.of());
        validateAllIgnoringNulls();
    }

    protected abstract IPlannedStuffValidator<T> initPlannedStuffValidator();
    private void validateAllIgnoringNulls(){
        for(var container : plannedStuffContainers){
            if(container.getId() == null)
                throw new UnableToExecuteQueryException(String.format("Updating %s {%s} can not have empty id",stuffName,container));
            stuffValidator.validateUsing(container, List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
            if(container.getName() != null)
                stuffValidator.validateUsing(container, NAME_VALIDATION_VALIDATOR_CONFIG.getComponentsIds());
            if(container.getUserAccount() != null)
                stuffValidator.validateUsing(container, List.of(PS_USER_ACCOUNT_VALIDATOR_COMPONENT.getId()));
        }
    }
}
