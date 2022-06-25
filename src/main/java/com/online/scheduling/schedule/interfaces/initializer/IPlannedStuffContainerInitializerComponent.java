package com.online.scheduling.schedule.interfaces.initializer;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;

import java.util.List;

public interface IPlannedStuffContainerInitializerComponent<T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffContainerRequirements<T>> {
    List<T> initPartOfPlannedStuff(List<T> initialized,List<R_TO_IN> required);
}
