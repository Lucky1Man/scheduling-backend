package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.config.SchedulingBasicConfig;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.cleardata.plannedaction.ClearDeletingActionRequest;
import com.online.scheduling.schedule.cleardata.plannedaction.ClearSavingActionRequest;
import com.online.scheduling.schedule.cleardata.plannedaction.ClearUpdatingActionRequest;
import com.online.scheduling.schedule.repositories.PlannedActionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    public List<PlannedAction> save(Authentication authentication, List<PlannedAction> plannedActions) throws RuntimeException{
        UserAccountProvider.insertUserAccount(authentication, plannedActions);

        ClearSavingActionRequest clearSavingActionRequest = new ClearSavingActionRequest(plannedActions);
        plannedActions = clearSavingActionRequest.getPlannedActions();
        //action container related
        actionContainerService.save(authentication ,clearSavingActionRequest.getActionContainersToSave());
        actionContainerService.update(clearSavingActionRequest.getActionContainersToUpdate());
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
        return actionRepository.saveAll(plannedActions);
    }
    @Transactional
    public void save(PlannedAction action) throws RuntimeException{
        action = new ClearSavingActionRequest(List.of(action)).getPlannedActions().get(0);
        actionRepository.save(action);
    }

    public List<PlannedAction> get(String userEmail , Set<Long> ids) {
        return GetPlannedStuffFunc.getPlannedStuff(userEmail, ids, actionRepository);
    }

    @Transactional
    public void delete(List<Long> ids){
        ids = Set.copyOf(ids).stream().toList();
        ids = new ClearDeletingActionRequest(ids).getIds();
        actionRepository.deleteAllById(ids);
    }

    @Transactional
    public List<PlannedAction> update(Authentication authentication ,List<PlannedAction> actions) {
        var result = new ArrayList<PlannedAction>();
        ClearUpdatingActionRequest clearUpdatingActionRequest = new ClearUpdatingActionRequest(actions);
        actions = clearUpdatingActionRequest.getActions();
        actionContainerService.update(clearUpdatingActionRequest.getActionContainersToUpdate());
        actionContainerService.save(authentication ,clearUpdatingActionRequest.getActionContainersToSave());
        for(var action : actions){
            PlannedAction currentActionFromDB = actionRepository.getById(action.getId());
            var plannedActionContainer = action.getPlannedActionContainer();
            if(plannedActionContainer != null)
                currentActionFromDB.setPlannedActionContainer(plannedActionContainer);
            var startsAt = action.getStartsAt();
            if(startsAt != null)
                currentActionFromDB.setStartsAt(startsAt);
            var endsAt = action.getEndsAt();
            if(endsAt != null)
                currentActionFromDB.setEndsAt(endsAt);
            var name = action.getName();
            if(name != null)
                currentActionFromDB.setName(name);
            var description = action.getDescription();
            if(description != null)
                currentActionFromDB.setDescription(description);
            var remindBefore = action.getRemindBefore();
            if(remindBefore != null)
                currentActionFromDB.setRemindBefore(remindBefore);
            result.add(currentActionFromDB);
            action.copyProperties(currentActionFromDB);
        }
        return result;
    }
}
