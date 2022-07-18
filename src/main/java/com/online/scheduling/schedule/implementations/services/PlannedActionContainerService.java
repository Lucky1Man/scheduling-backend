package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffAccordingToGivenIdsAndUserEmail;
import com.online.scheduling.schedule.cleardata.plannedactioncontainer.ClearDeletingActionContainerRequest;
import com.online.scheduling.schedule.cleardata.plannedactioncontainer.ClearSavingActionContainerRequest;
import com.online.scheduling.schedule.cleardata.plannedactioncontainer.ClearUpdatingActionContainerRequest;
import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.enums.RequestTypes;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.repositories.PlannedActionContainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.online.scheduling.schedule.carriedoutfunctions.CheckIfContainsPlannedStuffHolders.checkIfContains;
import static com.online.scheduling.schedule.carriedoutfunctions.GetAffectedPlannedStuffHolders.getAffectedPlannedStuffHolders;
import static com.online.scheduling.schedule.carriedoutfunctions.CheckIfUpdatingOrDeletionRequestContainsBasicPlannedStuffContainer.checkIfContainsBasicPlannedStuffContainer;

@Service
public class PlannedActionContainerService {
    private final String plannedStuffName = "planned action container";
    private final PlannedActionContainerRepository actionContainerRepository;

    public PlannedActionContainerService(
            PlannedActionContainerRepository actionContainerRepository) {
        this.actionContainerRepository = actionContainerRepository;
    }

    @Transactional
    public List<PlannedActionContainer> save(Authentication authentication, List<PlannedActionContainer> actionContainers) throws IllegalStateException{
        UserAccountProvider.insertUserAccountIfNeeded(authentication, actionContainers);
        actionContainers = new ClearSavingActionContainerRequest(actionContainers).getPlannedStuffContainers();
        return actionContainerRepository.saveAll(actionContainers);
    }
    @Transactional
    public void save(PlannedActionContainer actionContainer) throws IllegalStateException{
        actionContainer = new ClearSavingActionContainerRequest(List.of(actionContainer)).getPlannedStuffContainers().get(0);
        actionContainerRepository.save(actionContainer);
    }

    public Optional<PlannedActionContainer> getPlannedActionContainerByName(String name){
        return actionContainerRepository.findByName(name);
    }
    public List<PlannedActionContainer> get(String userEmail , Set<Long> ids) {
        return GetPlannedStuffAccordingToGivenIdsAndUserEmail.getPlannedStuff(userEmail, ids, actionContainerRepository);
    }
    @Transactional
    public Map<Long, List<PlannedAction>> delete(List<Long> ids){
        ids = Set.copyOf(ids).stream().toList();
        checkIfContainsBasicPlannedStuffContainer(ids, RequestTypes.DELETE, plannedStuffName);
        ids = new ClearDeletingActionContainerRequest(ids).getIds();

        Map<Long, List<PlannedAction>> affectedPlannedStuffHolders = getAffectedPlannedStuffHolders(ids, actionContainerRepository);
        if(!checkIfContains(ids , affectedPlannedStuffHolders))
            actionContainerRepository.deleteAllById(ids);
        return affectedPlannedStuffHolders;
    }
    @Transactional
    public List<PlannedActionContainer> update(List<PlannedActionContainer> actionContainers) {
        checkIfContainsBasicPlannedStuffContainer(RequestTypes.UPDATE, actionContainers, plannedStuffName);
        var result = new ArrayList<PlannedActionContainer>();
        actionContainers = new ClearUpdatingActionContainerRequest(actionContainers).getPlannedStuffContainers();
        for(var actionContainer : actionContainers){
            PlannedActionContainer currentActionContainerFromDB = actionContainerRepository.getById(actionContainer.getId());
            String bgColor = actionContainer.getBgColor();
            if(bgColor != null)
                currentActionContainerFromDB.setBgColor(bgColor);
            String name = actionContainer.getName();
            if(name != null)
                currentActionContainerFromDB.setName(name);
            result.add(currentActionContainerFromDB);
            actionContainer.copyProperties(currentActionContainerFromDB);
        }
        return result;
    }
}
