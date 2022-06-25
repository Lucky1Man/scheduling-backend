package com.online.scheduling.schedule.implementations.initializers;

import com.online.scheduling.schedule.implementations.initializers.plannedaction.PlannedActionInitializer;
import com.online.scheduling.schedule.implementations.initializers.plannedactioncontainer.PlannedActionContainerInitializer;
import com.online.scheduling.schedule.implementations.initializers.plannedday.PlannedDayInitializer;
import com.online.scheduling.schedule.implementations.initializers.planneddaycontainer.PlannedDayContainerInitializer;
import com.online.scheduling.schedule.implementations.initializers.schedulecontainer.ScheduleContainerInitializer;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class InitializersHolder {
    private final PlannedActionContainerInitializer actionContainerInitializer;
    private final PlannedDayContainerInitializer dayContainerInitializer;
    private final PlannedActionInitializer actionInitializer;
    private final PlannedDayInitializer dayInitializer;
    private final ScheduleContainerInitializer scheduleContainerInitializer;
    public InitializersHolder(
            PlannedActionContainerInitializer actionContainerInitializer,
            PlannedDayContainerInitializer dayContainerInitializer,
            PlannedActionInitializer actionInitializer,
            PlannedDayInitializer dayInitializer,
            ScheduleContainerInitializer scheduleContainerInitializer) {
        this.actionContainerInitializer = actionContainerInitializer;
        this.dayContainerInitializer = dayContainerInitializer;
        this.actionInitializer = actionInitializer;
        this.dayInitializer = dayInitializer;
        this.scheduleContainerInitializer = scheduleContainerInitializer;
    }
}
