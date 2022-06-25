package com.online.scheduling.schedule.interfaces.initializer;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;

public interface IPlannedStuffContainerRequirements<T extends IPlannedStuff<T>> {
    T getPlannedStuffContainer();
    void setPlannedStuffContainer(T container);
}
