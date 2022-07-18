package com.online.scheduling.schedule.interfaces.validator;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;

import java.util.List;

public interface IPlannedStuffValidator<T extends IPlannedStuff<T>> {
    boolean validate(T validated);
    boolean validateAll(List<T> validated);
    boolean validateUsing(T validated, List<Long> useToValidate);
    boolean validateAllUsing(List<T> validated, List<Long> useToValidate);
    boolean validateExcluding(T validated, List<Long> exclude);
    boolean validateAllExcluding(List<T> validated, List<Long> exclude);

}
