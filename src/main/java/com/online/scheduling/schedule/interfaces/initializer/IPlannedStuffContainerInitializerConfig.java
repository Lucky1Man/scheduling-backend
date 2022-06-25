package com.online.scheduling.schedule.interfaces.initializer;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;

import java.util.List;

public interface IPlannedStuffContainerInitializerConfig<T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffContainerRequirements<T>> {
    List<IPlannedStuffContainerInitializerComponent<T, R_TO_IN>> getContainerInitializerComponents();
}
