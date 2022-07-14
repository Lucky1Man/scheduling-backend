package com.online.scheduling.schedule.implementations.initializers.plannedactioncontainer;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer.PlannedStuffContainerInitializerTemplate;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerComponent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlannedActionContainerInitializer {
    private final PlannedStuffContainerInitializerTemplate<PlannedActionContainer, PlannedAction> plannedActionContainerInitializer;

    public PlannedActionContainerInitializer(
            List<IPlannedStuffContainerInitializerComponent<PlannedActionContainer, PlannedAction>> stuffContainerInitializerComponents){
        this.plannedActionContainerInitializer = new PlannedStuffContainerInitializerTemplate<>(stuffContainerInitializerComponents);
    }
    public List<PlannedActionContainer> doInit(List<PlannedActionContainer> initialized, List<PlannedAction> requiredToInit)
            throws RuntimeException{
        return plannedActionContainerInitializer.getStuffContainerInitializer().doInit(initialized,requiredToInit);
    }
}
