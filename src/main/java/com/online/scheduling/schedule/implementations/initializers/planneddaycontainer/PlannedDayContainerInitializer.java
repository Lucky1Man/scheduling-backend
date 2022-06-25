package com.online.scheduling.schedule.implementations.initializers.planneddaycontainer;

import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer.PlannedStuffContainerInitializerTemplate;
import com.online.scheduling.schedule.implementations.validators.ValidatorsHolder;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerComponent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlannedDayContainerInitializer {
    private final PlannedStuffContainerInitializerTemplate<PlannedDayContainer, PlannedDay> plannedActionContainerInitializer;

    public PlannedDayContainerInitializer(
            List<IPlannedStuffContainerInitializerComponent<PlannedDayContainer, PlannedDay>> stuffContainerInitializerComponents,
            ValidatorsHolder validatorsHolder){
        this.plannedActionContainerInitializer = new PlannedStuffContainerInitializerTemplate<>(
                stuffContainerInitializerComponents,
                validatorsHolder.getDayContainerValidator()
        );
    }

    public List<PlannedDayContainer> doInit(List<PlannedDayContainer> initialized, List<PlannedDay> requiredToInit)
            throws RuntimeException{
        return plannedActionContainerInitializer.getStuffContainerInitializer().doInit(initialized,requiredToInit);
    }
}
