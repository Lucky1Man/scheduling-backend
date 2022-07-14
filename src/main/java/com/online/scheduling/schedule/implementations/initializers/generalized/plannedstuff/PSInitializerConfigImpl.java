package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuff;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerConfig;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;

import java.util.List;

public class PSInitializerConfigImpl <T extends IPlannedStuff<T>, R_TO_CONT_IN extends IPlannedStuffRequirements<T>>
        implements IPlannedStuffInitializerConfig<T, R_TO_CONT_IN> {

    private final List<IPlannedStuffInitializerComponent<T, R_TO_CONT_IN>> stuffInitializerComponents;

    public PSInitializerConfigImpl(
            List<IPlannedStuffInitializerComponent<T, R_TO_CONT_IN>> stuffInitializerComponents) {
        this.stuffInitializerComponents = stuffInitializerComponents;
    }

    @Override
    public List<IPlannedStuffInitializerComponent<T, R_TO_CONT_IN>> getInitializerComponents() {
        return stuffInitializerComponents;
    }
}
