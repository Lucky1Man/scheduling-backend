package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.config.SchedulingBasicConfig;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.SchedulingProvider;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.models.plannedaction.ClearSavingActionRequest;
import com.online.scheduling.schedule.models.plannedday.ClearDeletingDayRequest;
import com.online.scheduling.schedule.models.plannedday.ClearSavingDayRequest;
import com.online.scheduling.schedule.models.plannedday.ClearUpdatingDayRequest;
import com.online.scheduling.schedule.repositories.PlannedDayRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.online.scheduling.schedule.enums.PlannedStuffValidatorComponentsIds.PS_ID_VALIDATOR_COMPONENT;

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
    public void saveAll(Authentication authentication, List<PlannedDay> plannedDays){
        plannedDays = UserAccountProvider.insertUserAccount(authentication, plannedDays);

        ClearSavingDayRequest clearSavingDayRequest = new ClearSavingDayRequest(plannedDays);
        plannedDays = clearSavingDayRequest.getPlannedDays();
        //day container related
        dayContainerService.saveAll(authentication ,clearSavingDayRequest.getDayContainersToSave());
        dayContainerService.update(clearSavingDayRequest.getDayContainersToUpdate());
        //
        //planned action related
        actionService.saveAll(authentication ,clearSavingDayRequest.getPlannedActionsToSave());
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

        dayRepository.saveAll(plannedDays);
    }
    @Transactional
    public void save(PlannedDay day){
        day = new ClearSavingDayRequest(List.of(day)).getPlannedDays().get(0);
        dayRepository.save(day);
    }
    public List<PlannedDay> getDays(String userEmail , Set<Long> ids) {
        return GetPlannedStuffFunc.getPlannedStuff(userEmail, ids, dayRepository);
    }

    @Transactional
    public void delete(List<Long> ids) {
        ids = new ClearDeletingDayRequest(ids).getIds();
        dayRepository.deleteAllById(ids);
    }


    @Transactional
    public void update(Authentication authentication ,List<PlannedDay> plannedDays) {
        plannedDays = UserAccountProvider.insertUserAccount(authentication, plannedDays);

        ClearUpdatingDayRequest clearUpdatingDayRequest = new ClearUpdatingDayRequest(plannedDays);
        plannedDays = clearUpdatingDayRequest.getPlannedDays();
        //day container related
        dayContainerService.saveAll(authentication ,clearUpdatingDayRequest.getPlannedDayContainersToSave());
        dayContainerService.update(clearUpdatingDayRequest.getPlannedDayContainersToUpdate());
        //
        //planned action related
        actionService.saveAll(authentication ,clearUpdatingDayRequest.getPlannedActionsToSave());
        actionService.update(authentication ,clearUpdatingDayRequest.getPlannedActionsToUpdate());
        //
        List<List<PlannedAction>> idsOfPlannedActionsToBeRemovedFromDays = clearUpdatingDayRequest.getIdsOfPlannedActionsToBeRemovedFromDays();
        for(var action : idsOfPlannedActionsToBeRemovedFromDays)
            SchedulingProvider.getValidator().getActionValidator().validateAllUsing(action,List.of(PS_ID_VALIDATOR_COMPONENT.getId()));
        int i = 0;
        for(var day : plannedDays){
            PlannedDay currentDay = dayRepository.getById(day.getId());
            PlannedDayContainer plannedDayContainer = day.getPlannedDayContainer();
            if(plannedDayContainer != null)
                currentDay.setPlannedDayContainer(plannedDayContainer);
            String name = day.getName();
            if(name != null)
                currentDay.setName(name);
            LocalDate date = day.getDate();
            if(date != null)
                currentDay.setDate(date);
            DayOfWeek dayOfWeek = day.getDayOfWeek();
            if(dayOfWeek != null)
                currentDay.setDayOfWeek(dayOfWeek);
            List<PlannedAction> plannedActions = day.getPlannedActions();
            if(plannedActions != null && !plannedActions.isEmpty())
                currentDay.getPlannedActions().addAll(clearUpdatingDayRequest.getActionsToBeAdded());

            //removing marked stuff
            List<PlannedAction> currentDayPlannedActions = currentDay.getPlannedActions();
            for(int k = 0; k < currentDayPlannedActions.size(); k++){
                var action = currentDayPlannedActions.get(k);
                List<PlannedAction> actionsToBeRemoved = idsOfPlannedActionsToBeRemovedFromDays.get(i);
                boolean wasRemoved = false;
                for (PlannedAction idHoled : actionsToBeRemoved) {
                    if (idHoled.getId().equals(action.getId())){
                        currentDay.getPlannedActions().remove(k);
                        wasRemoved = true;
                        break;
                    }

                }
                if(wasRemoved)
                    k--;
            }
            i++;
        }
    }
}
