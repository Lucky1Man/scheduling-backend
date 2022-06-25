package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuff;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerConfig;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;

import java.util.List;

public class PlannedStuffInitializerImpl <T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffRequirements<T>>{
    private final IPlannedStuffInitializerConfig<T, R_TO_IN> initializerConfig;

    public PlannedStuffInitializerImpl(
            IPlannedStuffInitializerConfig<T, R_TO_IN> initializerConfig) {
        this.initializerConfig = initializerConfig;

    }

    public List<T> doInit(List<T> initialized, List<R_TO_IN> requiredToInit){
        for(var component : initializerConfig.getInitializerComponents()){
            initialized = component.initPartOfPlannedStuff(initialized, requiredToInit);
        }
        return initialized;
    }


}
