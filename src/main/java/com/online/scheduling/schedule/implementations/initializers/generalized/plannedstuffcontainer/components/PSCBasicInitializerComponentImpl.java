package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer.components;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;

import java.util.ArrayList;
import java.util.List;

public class PSCBasicInitializerComponentImpl<T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffContainerRequirements<T>>
        implements IPlannedStuffContainerInitializerComponent<T, R_TO_IN> {

    @Override
    public List<T> initPartOfPlannedStuff(List<T> initialized,List<R_TO_IN> required) {
        if(initialized == null)
            initialized = new ArrayList<>();
        for(R_TO_IN RSTIComponent : required){
            T actionContainer = RSTIComponent.getPlannedStuffContainer();
            if(actionContainer != null && actionContainer.getId() == null)
                initialized.add(actionContainer);
        }
        return initialized;
    }
}
