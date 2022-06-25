package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.models.plannedactioncontainer.ClearDeletingActionContainerRequest;
import com.online.scheduling.schedule.models.plannedactioncontainer.ClearSavingActionContainerRequest;
import com.online.scheduling.schedule.models.plannedactioncontainer.ClearUpdatingActionContainerRequest;
import com.online.scheduling.schedule.repositories.PlannedActionContainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlannedActionContainerService {
    private final PlannedActionContainerRepository actionContainerRepository;

    public PlannedActionContainerService(
            PlannedActionContainerRepository actionContainerRepository) {
        this.actionContainerRepository = actionContainerRepository;
    }

    @Transactional
    public void saveAll(Authentication authentication, List<PlannedActionContainer> actionContainers) throws IllegalStateException{
        actionContainers = UserAccountProvider.insertUserAccount(authentication, actionContainers);
        actionContainers = new ClearSavingActionContainerRequest(actionContainers).getPlannedStuffContainers();
        actionContainerRepository.saveAll(actionContainers);
    }
    @Transactional
    public void save(PlannedActionContainer actionContainer) throws IllegalStateException{
        actionContainer = new ClearSavingActionContainerRequest(List.of(actionContainer)).getPlannedStuffContainers().get(0);
        actionContainerRepository.save(actionContainer);
    }

    public Optional<PlannedActionContainer> getPlannedActionContainerByName(String name){
        return actionContainerRepository.findByName(name);
    }
    public List<PlannedActionContainer> getActionContainers(String userEmail , Set<Long> ids) {
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
        ids = new ClearDeletingActionContainerRequest(ids).getIds();
        if(ids.contains(1L))
            throw new UnableToExecuteQueryException("You can not delete basic planned action container");
        actionContainerRepository.deleteAllById(ids);
    }
    @Transactional
    public void updateAll(List<PlannedActionContainer> actionContainers) {
        actionContainers = new ClearUpdatingActionContainerRequest(actionContainers).getPlannedStuffContainers();
        for(var cont : actionContainers)
            if(cont.getId() == 1L)
                throw new UnableToExecuteQueryException("You can not update basic planned action container");
        for(var container : actionContainers){
            PlannedActionContainer actionContainer = actionContainerRepository.getById(container.getId());
            String bgColor = container.getBgColor();
            if(bgColor != null)
                actionContainer.setBgColor(bgColor);
            String name = container.getName();
            if(name != null)
                actionContainer.setName(name);
        }
    }
}
