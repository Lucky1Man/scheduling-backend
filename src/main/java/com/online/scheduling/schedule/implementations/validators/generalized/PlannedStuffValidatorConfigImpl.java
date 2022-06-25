package com.online.scheduling.schedule.implementations.validators.generalized;

import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorComponent;
import com.online.scheduling.schedule.interfaces.validator.IPlannedStuffValidatorConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlannedStuffValidatorConfigImpl<T> implements IPlannedStuffValidatorConfig<T> {
    private final List<IPlannedStuffValidatorComponent<T>> plannedStuffValidatorComponents;

    public PlannedStuffValidatorConfigImpl(
            List<IPlannedStuffValidatorComponent<T>> plannedStuffValidatorComponents) {
        this.plannedStuffValidatorComponents = plannedStuffValidatorComponents;
    }

    @Override
    public List<IPlannedStuffValidatorComponent<T>> getValidatorComponents() {
        return plannedStuffValidatorComponents;
    }
}
