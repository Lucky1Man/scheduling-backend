package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.config.SchedulingBasicConfig;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.cleardata.schedule.ClearDeletingScheduleRequest;
import com.online.scheduling.schedule.cleardata.schedule.ClearSavingScheduleRequest;
import com.online.scheduling.schedule.cleardata.schedule.ClearUpdatingScheduleRequest;
import com.online.scheduling.schedule.repositories.ScheduleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.online.scheduling.schedule.carriedoutfunctions.GetInnerStuffToBeAddedToMainStuff.getStuffToBeAdded;
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
    public List<Schedule> save(Authentication authentication, List<Schedule> schedules){
        UserAccountProvider.insertUserAccount(authentication, schedules);

        ClearSavingScheduleRequest clearSavingScheduleRequest = new ClearSavingScheduleRequest(schedules);
        schedules = clearSavingScheduleRequest.getSchedules();
        // schedule container related
        scheduleContainerService.save(authentication ,clearSavingScheduleRequest.getScheduleContainersToSave());
        scheduleContainerService.update(clearSavingScheduleRequest.getScheduleContainersToUpdate());
        //
        // planned day related
        dayService.save(authentication ,clearSavingScheduleRequest.getPlannedDaysToSave());
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

        return scheduleRepository.saveAll(schedules);
    }

    public List<Schedule> get(String userEmail , Set<Long> ids) {
        return GetPlannedStuffFunc.getPlannedStuff(userEmail,ids,scheduleRepository);
    }

    public void delete(List<Long> ids) {
        ids = new ClearDeletingScheduleRequest(ids).getIds();
        scheduleRepository.deleteAllById(ids);
    }

    @Transactional
    public List<Schedule> update(Authentication authentication, List<Schedule> schedules) {
        var result = new ArrayList<Schedule>();
        ClearUpdatingScheduleRequest clearUpdatingScheduleRequest = new ClearUpdatingScheduleRequest(schedules);
        schedules = clearUpdatingScheduleRequest.getSchedules();
        //planned day related
        dayService.save(authentication, clearUpdatingScheduleRequest.getPlannedDaysToSave());
        dayService.update(authentication, clearUpdatingScheduleRequest.getPlannedDaysToUpdate());
        //
        //schedule container related
        scheduleContainerService.save(authentication, clearUpdatingScheduleRequest.getScheduleContainersToSave());
        scheduleContainerService.update(clearUpdatingScheduleRequest.getScheduleContainersToUpdate());
        //
        var daysToRemove = clearUpdatingScheduleRequest.getIdsOfDaysToBeRemovedFromSchedules();
        int i = 0;
        for(var schedule : schedules){
            Schedule currentScheduleFromDB = scheduleRepository.getById(schedule.getId());
            schedule.copyProperties(currentScheduleFromDB);
            var scheduleContainer = schedule.getScheduleContainer();
            if(scheduleContainer != null)
                currentScheduleFromDB.setScheduleContainer(scheduleContainer);
            String name = schedule.getName();
            if(name != null)
                currentScheduleFromDB.setName(name);
            List<PlannedDay> days = schedule.getDays();
            if(days != null && !days.isEmpty())
                currentScheduleFromDB.getDays().addAll(getStuffToBeAdded(currentScheduleFromDB, schedule.getDays()));
            var currentSchedulePlannedDays = currentScheduleFromDB.getDays();
            for(int k = 0; k < currentSchedulePlannedDays.size(); k++){
                var day = currentSchedulePlannedDays.get(k);
                var daysToBeRemoved = daysToRemove.get(i);
                boolean wasRemoved = false;
                for (var idHoled : daysToBeRemoved) {
                    if (idHoled.getId().equals(day.getId())){
                        currentScheduleFromDB.getDays().remove(k);
                        wasRemoved = true;
                    }
                }
                if(wasRemoved)
                    k--;
            }
            i++;
            result.add(currentScheduleFromDB);
        }
        return result;
    }
}
