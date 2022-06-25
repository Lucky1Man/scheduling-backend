package com.online.scheduling.schedule.interfaces.validator;

import java.util.List;
import java.util.Map;

public interface IPlannedStuffValidatorConfig<T> {
    List<IPlannedStuffValidatorComponent<T>> getValidatorComponents();
}
