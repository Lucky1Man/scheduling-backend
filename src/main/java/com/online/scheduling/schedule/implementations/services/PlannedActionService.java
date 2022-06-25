package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.config.SchedulingBasicConfig;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.models.plannedaction.ClearDeletingActionRequest;
import com.online.scheduling.schedule.models.plannedaction.ClearSavingActionRequest;
import com.online.scheduling.schedule.models.plannedaction.ClearUpdatingActionRequest;
import com.online.scheduling.schedule.models.plannedactioncontainer.ClearSavingActionContainerRequest;
import com.online.scheduling.schedule.repositories.PlannedActionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlannedActionService {
    private final SchedulingBasicConfig basicConfig;
    private final PlannedActionRepository actionRepository;
    private final PlannedActionContainerService actionContainerService;

    public PlannedActionService(
            SchedulingBasicConfig basicConfig,
            PlannedActionRepository actionRepository,
            PlannedActionContainerService actionContainerService) {
        this.basicConfig = basicConfig;
        this.actionRepository = actionRepository;
        this.actionContainerService = actionContainerService;
    }

    @Transactional
    public void saveAll(Authentication authentication, List<PlannedAction> plannedActions) throws RuntimeException{

        plannedActions = UserAccountProvider.insertUserAccount(authentication, plannedActions);

        ClearSavingActionRequest clearSavingActionRequest = new ClearSavingActionRequest(plannedActions);
        plannedActions = clearSavingActionRequest.getPlannedActions();
        //action container related
        actionContainerService.saveAll(authentication ,clearSavingActionRequest.getActionContainersToSave());
        actionContainerService.updateAll(clearSavingActionRequest.getActionContainersToUpdate());
        //
        plannedActions = plannedActions.stream().peek(
                (action) ->{
                    if(action.getPlannedActionContainer() == null) {
                        action.setPlannedActionContainer(
                                actionContainerService.getPlannedActionContainerByName(
                                        basicConfig.getBasicPlannedActionContainerName()
                                ).get());
                    }
                }
        ).collect(Collectors.toList());
        actionRepository.saveAll(plannedActions);
    }
    @Transactional
    public void save(PlannedAction action) throws RuntimeException{
        action = new ClearSavingActionRequest(List.of(action)).getPlannedActions().get(0);
        actionRepository.save(action);
    }

    public List<PlannedAction> getActions(String userEmail , Set<Long> ids) {
        return GetPlannedStuffFunc.getPlannedStuff(userEmail, ids, actionRepository);
    }

    @Transactional
    public void delete(List<Long> ids){
        ids = new ClearDeletingActionRequest(ids).getIds();
        actionRepository.deleteAllById(ids);
    }

    @Transactional
    public void update(Authentication authentication ,List<PlannedAction> actions) {
        ClearUpdatingActionRequest clearUpdatingActionRequest = new ClearUpdatingActionRequest(actions);
        actions = clearUpdatingActionRequest.getActions();
        actionContainerService.updateAll(clearUpdatingActionRequest.getActionContainersToUpdate());
        actionContainerService.saveAll(authentication ,clearUpdatingActionRequest.getActionContainersToSave());
        for(var action : actions){
            PlannedAction current = actionRepository.getById(action.getId());
            var plannedActionContainer = action.getPlannedActionContainer();
            if(plannedActionContainer != null)
                current.setPlannedActionContainer(plannedActionContainer);
            var startsAt = action.getStartsAt();
            if(startsAt != null)
                current.setStartsAt(startsAt);
            var endsAt = action.getEndsAt();
            if(endsAt != null)
                current.setEndsAt(endsAt);
            var name = action.getName();
            if(name != null)
                current.setName(name);
            var description = action.getDescription();
            if(description != null)
                current.setDescription(description);
            var remindBefore = action.getRemindBefore();
            if(remindBefore != null)
                current.setRemindBefore(remindBefore);
        }
    }
}
