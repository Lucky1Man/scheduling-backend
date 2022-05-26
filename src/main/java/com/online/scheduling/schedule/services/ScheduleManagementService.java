package com.online.scheduling.schedule.services;

import com.online.scheduling.schedule.models.*;
import com.online.scheduling.schedule.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service("SchedulingManagementService")
public class ScheduleManagementService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleManagementService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public void saveSchedule(){
        scheduleRepository.save(ScheduleContainer.builder()
                        .schedule(Schedule.builder()
                                .name("first")
                                .members(null)
                                .owner(null)
                                .plans(PlansContainer.builder()
                                        .plans(Plans.builder()
                                                .plannedYears(List.of(
                                                        PlannedYearContainer.builder()
                                                                .plannedYear(PlannedYear.builder()
                                                                        .plannedMonths(List.of(
                                                                                PlannedMonthContainer.builder()
                                                                                        .plannedMonth(PlannedMonth.builder()
                                                                                                .plannedWeeks(List.of(
                                                                                                        PlannedWeekContainer.builder()
                                                                                                                .plannedWeek(PlannedWeek.builder()
                                                                                                                        .plannedDays(List.of(
                                                                                                                                PlannedDayContainer.builder()
                                                                                                                                        .plannedDay(
                                                                                                                                                PlannedDay.builder()
                                                                                                                                                        .date(LocalDate.now())
                                                                                                                                                        .dayOfWeek(DayOfWeek.TUESDAY)
                                                                                                                                                        .plannedActions(List.of(
                                                                                                                                                                PlannedActionContainer.builder()
                                                                                                                                                                        .plannedAction(PlannedAction.builder()
                                                                                                                                                                                .description("SomePlans")
                                                                                                                                                                                .endsAt(LocalTime.MAX)
                                                                                                                                                                                .name("Test Plan")
                                                                                                                                                                                .remindBefore(LocalTime.of(0,15))
                                                                                                                                                                                .startsAt(LocalTime.now())
                                                                                                                                                                                .build())
                                                                                                                                                                        .build()
                                                                                                                                                        ))
                                                                                                                                                        .build()
                                                                                                                                        )
                                                                                                                                        .build()
                                                                                                                        ))
                                                                                                                        .build())
                                                                                                                .build()
                                                                                                ))
                                                                                                .build())
                                                                                        .build()
                                                                        ))
                                                                        .build())
                                                                .build()
                                                ))
                                                .build())
                                        .build())
                                .build())
                .build());
    }
}
