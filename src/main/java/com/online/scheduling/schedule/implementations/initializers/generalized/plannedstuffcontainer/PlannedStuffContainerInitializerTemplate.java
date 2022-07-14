package com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer;

import com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer.components.PSCBasicInitializerComponentImpl;
import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerRequirements;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PlannedStuffContainerInitializerTemplate<T extends IPlannedStuff<T>, R_TO_IN extends IPlannedStuffContainerRequirements<T>> {
    @Setter
    private IPlannedStuffContainerInitializerConfig<T, R_TO_IN> stuffInitializerConfig;
    @Setter
    @Getter
    private PlannedStuffContainerInitializerImpl<T, R_TO_IN> stuffContainerInitializer;

    public PlannedStuffContainerInitializerTemplate(
            List<IPlannedStuffContainerInitializerComponent<T, R_TO_IN>> stuffContainerInitializerComponents){
        PSCBasicInitializerComponentImpl<T, R_TO_IN> component1 = new PSCBasicInitializerComponentImpl<>();
        stuffContainerInitializerComponents.add(0, component1);
        this.stuffInitializerConfig = new PSCInitializerConfigImpl<>(stuffContainerInitializerComponents);
        this.stuffContainerInitializer = new PlannedStuffContainerInitializerImpl<>(stuffInitializerConfig);
    }
}
