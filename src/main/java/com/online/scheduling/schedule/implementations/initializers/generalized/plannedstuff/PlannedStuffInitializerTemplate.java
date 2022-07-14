package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuff;

import com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuff.components.PSBasicInitializerComponentImpl;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerConfig;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffRequirements;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PlannedStuffInitializerTemplate <T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffRequirements<T>>{

    @Setter
    private IPlannedStuffInitializerConfig<T, R_TO_IN> stuffInitializerConfig;
    @Setter
    @Getter
    private PlannedStuffInitializerImpl<T, R_TO_IN> stuffContainerInitializer;

    public PlannedStuffInitializerTemplate(
            List<IPlannedStuffInitializerComponent<T, R_TO_IN>> stuffContainerInitializerComponents) {

        PSBasicInitializerComponentImpl<T, R_TO_IN> component1 = new PSBasicInitializerComponentImpl<>();
        stuffContainerInitializerComponents.add(0, component1);
        this.stuffInitializerConfig = new PSInitializerConfigImpl<>(stuffContainerInitializerComponents);
        this.stuffContainerInitializer = new PlannedStuffInitializerImpl<>(stuffInitializerConfig);
    }
}
