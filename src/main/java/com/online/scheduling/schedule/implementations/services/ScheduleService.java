package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.config.SchedulingBasicConfig;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.models.schedule.ClearDeletingScheduleRequest;
import com.online.scheduling.schedule.models.schedule.ClearSavingScheduleRequest;
import com.online.scheduling.schedule.models.schedule.ClearUpdatingScheduleRequest;
import com.online.scheduling.schedule.repositories.ScheduleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds.PS_ID_VALIDATOR_COMPONENT;

@Service
public class ScheduleService {
    private final SchedulingBasicConfig basicConfig;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleContainerService scheduleContainerService;
    private final PlannedDayService dayService;

    public ScheduleService(
            SchedulingBasicConfig basicConfig,
            ScheduleRepository scheduleRepository,
            ScheduleContainerService scheduleContainerService,
            PlannedDayService dayService) {
        this.basicConfig = basicConfig;
        this.scheduleRepository = scheduleRepository;
        this.scheduleContainerService = scheduleContainerService;
        this.dayService = dayService;
    }

    @Transactional
    public void saveAll(Authentication authentication, List<Schedule> schedules){
        schedules = UserAccountProvider.insertUserAccount(authentication, schedules);

        ClearSavingScheduleRequest clearSavingScheduleRequest = new ClearSavingScheduleRequest(schedules);
        schedules = clearSavingScheduleRequest.getSchedules();
        // schedule container related
        scheduleContainerService.saveAll(authentication ,clearSavingScheduleRequest.getScheduleContainersToSave());
        scheduleContainerService.update(clearSavingScheduleRequest.getScheduleContainersToUpdate());
        //
        // planned day related
        dayService.saveAll(authentication ,clearSavingScheduleRequest.getPlannedDaysToSave());
        dayService.update(authentication ,clearSavingScheduleRequest.getPlannedDaysToUpdate());
        //

        schedules = schedules.stream().peek(
                (schedule)->{
                    if(schedule.getScheduleContainer() == null){
                        schedule.setScheduleContainer(
                                scheduleContainerService.getPlannedDayContainerByName(
                                        basicConfig.getBasicScheduleContainerName()
                                ).get());
                    }
                }
        ).collect(Collectors.toList());

        scheduleRepository.saveAll(schedules);
    }

    public List<Schedule> getSchedules(String userEmail ,Set<Long> ids) {
        return GetPlannedStuffFunc.getPlannedStuff(userEmail,ids,scheduleRepository);
    }

    public void delete(List<Long> ids) {
        ids = new ClearDeletingScheduleRequest(ids).getIds();
        scheduleRepository.deleteAllById(ids);
    }

    @Transactional
    public void update(Authentication authentication,List<Schedule> schedules) {
        schedules = UserAccountProvider.insertUserAccount(authentication, schedules);

        ClearUpdatingScheduleRequest clearUpdatingScheduleRequest = new ClearUpdatingScheduleRequest(schedules);
        schedules = clearUpdatingScheduleRequest.getSchedules();
        //planned day related
        dayService.saveAll(authentication, clearUpdatingScheduleRequest.getPlannedDaysToSave());
        dayService.update(authentication, clearUpdatingScheduleRequest.getPlannedDaysToUpdate());
        //
        //schedule container related
        scheduleContainerService.saveAll(authentication, clearUpdatingScheduleRequest.getScheduleContainersToSave());
        scheduleContainerService.update(clearUpdatingScheduleRequest.getScheduleContainersToUpdate());
        //
        var daysToRemove = clearUpdatingScheduleRequest.getIdsOfDaysToBeRemovedFromSchedules();
        for(var days : daysToRemove)
            SchedulingProvider.getValidator().getDayValidator().validateAllUsing(days, List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
        int i = 0;
        for(var schedule : schedules){
            Schedule currentSchedule = scheduleRepository.getById(schedule.getId());
            var scheduleContainer = schedule.getScheduleContainer();
            if(scheduleContainer != null)
                currentSchedule.setScheduleContainer(scheduleContainer);
            String name = schedule.getName();
            if(name != null)
                currentSchedule.setName(name);
            List<PlannedDay> days = schedule.getDays();
            if(days != null && !days.isEmpty())
                currentSchedule.getDays().addAll(clearUpdatingScheduleRequest.getDaysToBeAdded());

            var currentSchedulePlannedDays = currentSchedule.getDays();
            for(int k = 0; k < currentSchedulePlannedDays.size(); k++){
                var day = currentSchedulePlannedDays.get(k);
                var daysToBeRemoved = daysToRemove.get(i);
                boolean wasRemoved = false;
                for (var idHoled : daysToBeRemoved) {
                    if (idHoled.getId().equals(day.getId())){
                        currentSchedule.getDays().remove(k);
                        wasRemoved = true;
                    }
                }
                if(wasRemoved)
                    k--;
            }
            i++;
        }
    }
}
