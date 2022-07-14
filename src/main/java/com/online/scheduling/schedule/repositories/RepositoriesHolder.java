package com.online.scheduling.schedule.repositories;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RepositoriesHolder {
    private final PlannedActionRepository actionRepository;
    private final PlannedActionContainerRepository actionContainerRepository;
    private final PlannedDayRepository dayRepository;
    private final PlannedDayContainerRepository dayContainerRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleContainerRepository scheduleContainerRepository;
    private final UserAccountRepository userAccountRepository;

    public RepositoriesHolder(PlannedActionRepository actionRepository,
                              PlannedActionContainerRepository actionContainerRepository,
                              PlannedDayRepository dayRepository,
                              PlannedDayContainerRepository dayContainerRepository,
                              ScheduleRepository scheduleRepository,
                              ScheduleContainerRepository scheduleContainerRepository,
                              UserAccountRepository userAccountRepository) {
        this.actionRepository = actionRepository;
        this.actionContainerRepository = actionContainerRepository;
        this.dayRepository = dayRepository;
        this.dayContainerRepository = dayContainerRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleContainerRepository = scheduleContainerRepository;
        this.userAccountRepository = userAccountRepository;
    }
}
