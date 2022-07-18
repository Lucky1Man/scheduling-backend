package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerConfig;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;

import java.util.List;

public class PSCInitializerConfigImpl<T extends IPlannedStuff<T>, R_TO_CONT_IN extends IPlannedStuffContainerRequirements<T>>
        implements IPlannedStuffContainerInitializerConfig<T, R_TO_CONT_IN> {
    private final List<IPlannedStuffContainerInitializerComponent<T, R_TO_CONT_IN>> initializerComponents;

    public PSCInitializerConfigImpl(
            List<IPlannedStuffContainerInitializerComponent<T, R_TO_CONT_IN>> initializerComponents) {
        this.initializerComponents = initializerComponents;
    }

    @Override
    public List<IPlannedStuffContainerInitializerComponent<T, R_TO_CONT_IN>> getContainerInitializerComponents() {
        return initializerComponents;
    }
}
