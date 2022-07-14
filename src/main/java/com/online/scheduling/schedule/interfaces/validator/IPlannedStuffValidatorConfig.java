package com.online.scheduling.schedule.interfaces.validator;

import java.util.List;

public interface IPlannedStuffValidatorConfig<T> {
    List<IPlannedStuffValidatorComponent<T>> getValidatorComponents();
}
