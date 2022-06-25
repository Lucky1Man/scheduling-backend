package com.online.scheduling.schedule.implementations.initializers.plannedaction;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuff.PlannedStuffInitializerTemplate;
import com.online.scheduling.schedule.implementations.validators.ValidatorsHolder;
import com.online.scheduling.schedule.implementations.validators.plannedaction.PlannedActionValidator;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerComponent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlannedActionInitializer {
    private final PlannedStuffInitializerTemplate<PlannedAction, PlannedDay> plannedActionInitializer;

    public PlannedActionInitializer(
            List<IPlannedStuffInitializerComponent<PlannedAction, PlannedDay>> stuffInitializerComponents,
            ValidatorsHolder validatorsHolder) {
        this.plannedActionInitializer = new PlannedStuffInitializerTemplate<>(
                stuffInitializerComponents,
                validatorsHolder.getActionValidator()
        );
    }

    public List<PlannedAction> doInit(List<PlannedAction> initialized, List<PlannedDay> requiredToInit){
        return plannedActionInitializer.getStuffContainerInitializer().doInit(initialized,requiredToInit);
    }
}
