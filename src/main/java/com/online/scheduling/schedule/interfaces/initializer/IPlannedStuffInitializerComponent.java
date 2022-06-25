package com.online.scheduling.schedule.interfaces.initializer;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;

import java.util.List;

public interface IPlannedStuffInitializerComponent<T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffRequirements<T>> {
    List<T> initPartOfPlannedStuff(List<T> initialized, List<R_TO_IN> required);
}
