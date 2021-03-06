package com.online.scheduling.schedule.config;

import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.repositories.PlannedActionContainerRepository;
import com.online.scheduling.schedule.repositories.PlannedDayContainerRepository;
import com.online.scheduling.schedule.repositories.ScheduleContainerRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Configuration
@Qualifier("SchedulingBasicConfig")
public class SchedulingBasicConfig {
    @Value("${scheduling.config.basic-planned-action-container}")
    @Getter
    private String basicPlannedActionContainerName;
    @Value("${scheduling.config.basic-planned-day-container}")
    @Getter
    private String basicPlannedDayContainerName;
    @Value("${scheduling.config.basic-schedule-container}")
    @Getter
    private String basicScheduleContainerName;
    private final PlannedActionContainerRepository actionContainerRepository;
    private final PlannedDayContainerRepository dayContainerRepository;
    private final ScheduleContainerRepository scheduleContainerRepository;

    public SchedulingBasicConfig(
            PlannedActionContainerRepository actionContainerRepository,
            PlannedDayContainerRepository dayContainerRepository,
            ScheduleContainerRepository scheduleContainerRepository) {
        this.actionContainerRepository = actionContainerRepository;
        this.dayContainerRepository = dayContainerRepository;
        this.scheduleContainerRepository = scheduleContainerRepository;
    }

    @PostConstruct
    protected void init(){
        Optional<PlannedActionContainer> actionContainer = actionContainerRepository.findByName(basicPlannedActionContainerName);
        if(actionContainer.isEmpty()) {
            actionContainerRepository.save(
                    PlannedActionContainer.builder()
                            .name(basicPlannedActionContainerName)
                            .build());
        }
        Optional<PlannedDayContainer> dayContainer = dayContainerRepository.findByName(basicPlannedDayContainerName);
        if(dayContainer.isEmpty()){
            dayContainerRepository.save(
                    PlannedDayContainer.builder()
                            .name(basicPlannedDayContainerName)
                            .build());
        }
        Optional<ScheduleContainer> scheduleContainer = scheduleContainerRepository.findByName(basicScheduleContainerName);
        if(scheduleContainer.isEmpty()){
            scheduleContainerRepository.save(
                    ScheduleContainer.builder()
                            .name(basicScheduleContainerName)
                            .build());
        }
    }
}
