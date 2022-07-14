package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.cleardata.plannedactioncontainer.ClearDeletingActionContainerRequest;
import com.online.scheduling.schedule.cleardata.plannedactioncontainer.ClearSavingActionContainerRequest;
import com.online.scheduling.schedule.cleardata.plannedactioncontainer.ClearUpdatingActionContainerRequest;
import com.online.scheduling.schedule.repositories.PlannedActionContainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class PlannedActionContainerService {
    private final PlannedActionContainerRepository actionContainerRepository;

    public PlannedActionContainerService(
            PlannedActionContainerRepository actionContainerRepository) {
        this.actionContainerRepository = actionContainerRepository;
    }

    @Transactional
    public List<PlannedActionContainer> save(Authentication authentication, List<PlannedActionContainer> actionContainers) throws IllegalStateException{
        UserAccountProvider.insertUserAccount(authentication, actionContainers);
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
        return GetPlannedStuffFunc.getPlannedStuff(userEmail, ids, actionContainerRepository);
//        List<PlannedActionContainer> actionContainers = actionContainerRepository.findAllByUserAccount_Owner_Email(userEmail);
//        if(ids == null || ids.isEmpty())
//            return actionContainers;
//        actionContainers = actionContainers.stream().filter(container -> {
//            for(var id : ids){
//                if(container.getId().equals(id))
//                    return true;
//            }
//            return false;
//        }).collect(Collectors.toList());
//        return actionContainers;
//        if(ids == null || ids.isEmpty())
//            return actionContainerRepository.findAll();
//        return actionContainerRepository.findAllById(ids);
    }
    @Transactional
    public void delete(List<Long> ids){
        ids = Set.copyOf(ids).stream().toList();
        if(ids.contains(1L))
            throw new UnableToExecuteQueryException("You can not delete basic planned action container");
        ids = new ClearDeletingActionContainerRequest(ids).getIds();
        actionContainerRepository.deleteAllById(ids);
    }
    @Transactional
    public List<PlannedActionContainer> update(List<PlannedActionContainer> actionContainers) {
        var result = new ArrayList<PlannedActionContainer>();
        for(var cont : actionContainers)
            if(cont.getId() == 1L)
                throw new UnableToExecuteQueryException("You can not update basic planned action container");
        actionContainers = new ClearUpdatingActionContainerRequest(actionContainers).getPlannedStuffContainers();
        for(var actionContainer : actionContainers){
            PlannedActionContainer currentActionContainerFromDB = actionContainerRepository.getById(actionContainer.getId());
            actionContainer.copyProperties(currentActionContainerFromDB);
            String bgColor = actionContainer.getBgColor();
            if(bgColor != null)
                currentActionContainerFromDB.setBgColor(bgColor);
            String name = actionContainer.getName();
            if(name != null)
                currentActionContainerFromDB.setName(name);
            result.add(currentActionContainerFromDB);
        }
        return result;
    }
}
