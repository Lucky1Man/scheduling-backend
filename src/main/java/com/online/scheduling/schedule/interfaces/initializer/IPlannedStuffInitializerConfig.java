package com.online.scheduling.schedule.interfaces.initializer;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;

import java.util.List;

public interface IPlannedStuffInitializerConfig <T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffRequirements<T>>{
    List<IPlannedStuffInitializerComponent<T, R_TO_IN>> getInitializerComponents();
}
