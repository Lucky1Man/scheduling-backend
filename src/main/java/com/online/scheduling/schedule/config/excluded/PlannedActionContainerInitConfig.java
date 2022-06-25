package com.online.scheduling.schedule.config.excluded;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerComponent;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerConfig;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PlannedActionContainerInitConfig implements
        IPlannedStuffInitializerConfig<PlannedActionContainer, PlannedAction> {
    private final List<IPlannedStuffContainerInitializerComponent<PlannedActionContainer, PlannedAction>> initializerComponents;

    public PlannedActionContainerInitConfig(
            List<IPlannedStuffContainerInitializerComponent<PlannedActionContainer, PlannedAction>> initializerComponents) {
        this.initializerComponents = initializerComponents;
    }

    @Override
    public List<IPlannedStuffContainerInitializerComponent<PlannedActionContainer, PlannedAction>> getInitializerComponents() {
        return initializerComponents;
    }
}
