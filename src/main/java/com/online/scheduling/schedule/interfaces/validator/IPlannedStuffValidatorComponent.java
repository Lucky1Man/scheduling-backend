package com.online.scheduling.schedule.interfaces.validator;

public interface IPlannedStuffValidatorComponent<T> {
    boolean isValid(T validated);
}
