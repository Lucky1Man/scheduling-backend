package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffAccordingToGivenIdsAndUserEmail;
import com.online.scheduling.schedule.config.SchedulingBasicConfig;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.cleardata.plannedday.ClearDeletingDayRequest;
import com.online.scheduling.schedule.cleardata.plannedday.ClearSavingDayRequest;
import com.online.scheduling.schedule.cleardata.plannedday.ClearUpdatingDayRequest;
import com.online.scheduling.schedule.repositories.PlannedDayRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.online.scheduling.schedule.carriedoutfunctions.CheckIfContainsPlannedStuffHolders.checkIfContains;
import static com.online.scheduling.schedule.carriedoutfunctions.GetAffectedPlannedStuffHolders.getAffectedPlannedStuffHolders;
import static com.online.scheduling.schedule.carriedoutfunctions.GetInnerStuffToBeAddedToMainStuff.getStuffToBeAdded;
import static com.online.scheduling.schedule.carriedoutfunctions.RemoveMarkedInnerStuffFromMainStuff.removeMarkedInnerStuffFromMainStuff;

@Service
public class PlannedDayService {
    private final SchedulingBasicConfig basicConfig;
    private final PlannedDayRepository dayRepository;
    private final PlannedDayContainerService dayContainerService;
    private final PlannedActionService actionService;

    public PlannedDayService(
            @Qualifier("SchedulingBasicConfig")
            SchedulingBasicConfig basicConfig,
            PlannedDayRepository dayRepository,
            PlannedDayContainerService dayContainerService,
            PlannedActionService actionService) {
        this.basicConfig = basicConfig;
        this.dayRepository = dayRepository;
        this.dayContainerService = dayContainerService;
        this.actionService = actionService;
    }
    @Transactional
    public List<PlannedDay> save(Authentication authentication, List<PlannedDay> plannedDays){
        UserAccountProvider.insertUserAccountIfNeeded(authentication, plannedDays);

        ClearSavingDayRequest clearSavingDayRequest = new ClearSavingDayRequest(plannedDays);
        plannedDays = clearSavingDayRequest.getPlannedDays();

        //planned action related
        actionService.save(authentication ,clearSavingDayRequest.getPlannedActionsToSave());
        actionService.update(authentication ,clearSavingDayRequest.getPlannedActionsToUpdate());

        //day container related
        dayContainerService.save(authentication ,clearSavingDayRequest.getDayContainersToSave());
        dayContainerService.update(clearSavingDayRequest.getDayContainersToUpdate());

        plannedDays = plannedDays.stream().peek(
                (day)->{
                    if(day.getPlannedDayContainer() == null){
                        day.setPlannedDayContainer(
                                dayContainerService.getPlannedDayContainerByName(
                                        basicConfig.getBasicPlannedDayContainerName()
                                ).get());
                    }
                }
        ).collect(Collectors.toList());

        return dayRepository.saveAll(plannedDays);
    }
    @Transactional
    public void save(PlannedDay day){
        day = new ClearSavingDayRequest(List.of(day)).getPlannedDays().get(0);
        dayRepository.save(day);
    }
    public List<PlannedDay> get(String userEmail , Set<Long> ids) {
        return GetPlannedStuffAccordingToGivenIdsAndUserEmail.getPlannedStuff(userEmail, ids, dayRepository);
    }

    @Transactional
    public Map<Long, List<Schedule>> delete(List<Long> ids) {
        ids = Set.copyOf(ids).stream().toList();
        ids = new ClearDeletingDayRequest(ids).getIds();

        Map<Long, List<Schedule>> affectedPlannedStuffHolders = getAffectedPlannedStuffHolders(ids, dayRepository);
        if(!checkIfContains(ids , affectedPlannedStuffHolders))
            dayRepository.deleteAllById(ids);
        return affectedPlannedStuffHolders;
    }


    @Transactional
    public List<PlannedDay> update(Authentication authentication , List<PlannedDay> days) {
        var result = new ArrayList<PlannedDay>();

        ClearUpdatingDayRequest clearUpdatingDayRequest = new ClearUpdatingDayRequest(days);
        days = clearUpdatingDayRequest.getPlannedDays();

        //planned action related
        actionService.save(authentication ,clearUpdatingDayRequest.getPlannedActionsToSave());
        actionService.update(authentication ,clearUpdatingDayRequest.getPlannedActionsToUpdate());

        //day container related
        dayContainerService.save(authentication ,clearUpdatingDayRequest.getPlannedDayContainersToSave());
        dayContainerService.update(clearUpdatingDayRequest.getPlannedDayContainersToUpdate());

        for(var dayFromRequest : days){
            PlannedDay currentDayFromDB = dayRepository.getById(dayFromRequest.getId());
            PlannedDayContainer plannedDayContainer = dayFromRequest.getPlannedDayContainer();
            if(plannedDayContainer != null)
                currentDayFromDB.setPlannedDayContainer(plannedDayContainer);
            String name = dayFromRequest.getName();
            if(name != null)
                currentDayFromDB.setName(name);
            LocalDate date = dayFromRequest.getDate();
            if(date != null)
                currentDayFromDB.setDate(date);
            DayOfWeek dayOfWeek = dayFromRequest.getDayOfWeek();
            if(dayOfWeek != null)
                currentDayFromDB.setDayOfWeek(dayOfWeek);
            List<PlannedAction> plannedActions = dayFromRequest.getPlannedActions();
            if(plannedActions != null){
                currentDayFromDB.getPlannedActions().addAll(getStuffToBeAdded(currentDayFromDB, dayFromRequest.getPlannedActions()));
            }
            removeMarkedInnerStuffFromMainStuff(currentDayFromDB, dayFromRequest.getPlannedActions());
            result.add(currentDayFromDB);
            dayFromRequest.copyProperties(currentDayFromDB);
        }
        return result;
    }
}
