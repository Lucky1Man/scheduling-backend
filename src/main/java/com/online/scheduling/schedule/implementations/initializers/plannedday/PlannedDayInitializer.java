package com.online.scheduling.schedule.implementations.initializers.plannedday;

import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuff.PlannedStuffInitializerTemplate;
import com.online.scheduling.schedule.implementations.validators.ValidatorsHolder;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffInitializerComponent;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class PlannedDayInitializer {
    private final PlannedStuffInitializerTemplate<PlannedDay, Schedule> plannedDayInitializer;

    public PlannedDayInitializer(
            List<IPlannedStuffInitializerComponent<PlannedDay, Schedule>> stuffInitializerComponents,
            ValidatorsHolder validatorsHolder) {
        this.plannedDayInitializer = new PlannedStuffInitializerTemplate<>(
                stuffInitializerComponents,
                validatorsHolder.getDayValidator()
        );
    }

    public List<PlannedDay> doInit(List<PlannedDay> initialized, List<Schedule> requiredToInit){
        return plannedDayInitializer.getStuffContainerInitializer().doInit(initialized,requiredToInit);
    }
}
