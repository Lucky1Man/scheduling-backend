package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.config.SchedulingBasicConfig;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.cleardata.plannedday.ClearDeletingDayRequest;
import com.online.scheduling.schedule.cleardata.plannedday.ClearSavingDayRequest;
import com.online.scheduling.schedule.cleardata.plannedday.ClearUpdatingDayRequest;
import com.online.scheduling.schedule.repositories.PlannedDayRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.online.scheduling.schedule.carriedoutfunctions.GetInnerStuffToBeAddedToMainStuff.getStuffToBeAdded;

@Service
public class PlannedDayService {
    private final SchedulingBasicConfig basicConfig;
    private final PlannedDayRepository dayRepository;
    private final PlannedDayContainerService dayContainerService;
    private final PlannedActionService actionService;

    public PlannedDayService(
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
        UserAccountProvider.insertUserAccount(authentication, plannedDays);

        ClearSavingDayRequest clearSavingDayRequest = new ClearSavingDayRequest(plannedDays);
        plannedDays = clearSavingDayRequest.getPlannedDays();
        //day container related
        dayContainerService.save(authentication ,clearSavingDayRequest.getDayContainersToSave());
        dayContainerService.update(clearSavingDayRequest.getDayContainersToUpdate());
        //
        //planned action related
        actionService.save(authentication ,clearSavingDayRequest.getPlannedActionsToSave());
        actionService.update(authentication ,clearSavingDayRequest.getPlannedActionsToUpdate());
        //
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
        return GetPlannedStuffFunc.getPlannedStuff(userEmail, ids, dayRepository);
    }

    @Transactional
    public void delete(List<Long> ids) {
        ids = new ClearDeletingDayRequest(ids).getIds();
        dayRepository.deleteAllById(ids);
    }


    @Transactional
    public List<PlannedDay> update(Authentication authentication , List<PlannedDay> days) {
        var result = new ArrayList<PlannedDay>();

        ClearUpdatingDayRequest clearUpdatingDayRequest = new ClearUpdatingDayRequest(days);
        days = clearUpdatingDayRequest.getPlannedDays();
        //day container related
        dayContainerService.save(authentication ,clearUpdatingDayRequest.getPlannedDayContainersToSave());
        dayContainerService.update(clearUpdatingDayRequest.getPlannedDayContainersToUpdate());

        //planned action related
        actionService.save(authentication ,clearUpdatingDayRequest.getPlannedActionsToSave());
        actionService.update(authentication ,clearUpdatingDayRequest.getPlannedActionsToUpdate());

        List<List<PlannedAction>> idsOfPlannedActionsToBeRemovedFromDays = clearUpdatingDayRequest.getIdsOfPlannedActionsToBeRemovedFromDays();
        int i = 0;
        for(var day : days){
            PlannedDay currentDayFromDB = dayRepository.getById(day.getId());
            PlannedDayContainer plannedDayContainer = day.getPlannedDayContainer();
            if(plannedDayContainer != null)
                currentDayFromDB.setPlannedDayContainer(plannedDayContainer);
            String name = day.getName();
            if(name != null)
                currentDayFromDB.setName(name);
            LocalDate date = day.getDate();
            if(date != null)
                currentDayFromDB.setDate(date);
            DayOfWeek dayOfWeek = day.getDayOfWeek();
            if(dayOfWeek != null)
                currentDayFromDB.setDayOfWeek(dayOfWeek);
            List<PlannedAction> plannedActions = day.getPlannedActions();
            if(plannedActions != null){
                currentDayFromDB.getPlannedActions().addAll(getStuffToBeAdded(currentDayFromDB, day.getPlannedActions()));
            }
            //removing marked stuff
            List<PlannedAction> currentDayPlannedActions = currentDayFromDB.getPlannedActions();
            for(int k = 0; k < currentDayPlannedActions.size(); k++){
                var action = currentDayPlannedActions.get(k);
                List<PlannedAction> actionsToBeRemoved = idsOfPlannedActionsToBeRemovedFromDays.get(i);
                boolean wasRemoved = false;
                for (PlannedAction idHoled : actionsToBeRemoved) {
                    if (idHoled.getId().equals(action.getId())){
                        currentDayFromDB.getPlannedActions().remove(k);
                        wasRemoved = true;
                        break;
                    }

                }
                if(wasRemoved)
                    k--;
            }
            i++;
            result.add(currentDayFromDB);
            day.copyProperties(currentDayFromDB);
        }
        return result;
    }
}
