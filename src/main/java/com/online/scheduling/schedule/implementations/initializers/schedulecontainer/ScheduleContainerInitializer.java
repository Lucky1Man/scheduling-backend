package com.online.scheduling.schedule.implementations.initializers.schedulecontainer;

import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.implementations.initializers.generalized.plannedstuffcontainer.PlannedStuffContainerInitializerTemplate;
import com.online.scheduling.schedule.interfaces.initializer.IPlannedStuffContainerInitializerComponent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleContainerInitializer {
    private final PlannedStuffContainerInitializerTemplate<ScheduleContainer, Schedule> scheduleContainerInitializer;

    public ScheduleContainerInitializer(
            List<IPlannedStuffContainerInitializerComponent<ScheduleContainer, Schedule>> stuffContainerInitializerComponents){
        this.scheduleContainerInitializer = new PlannedStuffContainerInitializerTemplate<>(stuffContainerInitializerComponents);
    }

    public List<ScheduleContainer> doInit(List<ScheduleContainer> initialized, List<Schedule> requiredToInit)
            throws RuntimeException{
        return scheduleContainerInitializer.getStuffContainerInitializer().doInit(initialized,requiredToInit);
    }
}
