package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.models.plannedactioncontainer.ClearSavingActionContainerRequest;
import com.online.scheduling.schedule.models.planneddaycontainer.ClearDeletingDayContainerRequest;
import com.online.scheduling.schedule.models.planneddaycontainer.ClearSavingDayContainerRequest;
import com.online.scheduling.schedule.models.planneddaycontainer.ClearUpdatingDayContainerRequest;
import com.online.scheduling.schedule.repositories.PlannedDayContainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PlannedDayContainerService {
    private final PlannedDayContainerRepository dayContainerRepository;

    public PlannedDayContainerService(
            PlannedDayContainerRepository dayContainerRepository) {
        this.dayContainerRepository = dayContainerRepository;
    }

    @Transactional
    public void saveAll(Authentication authentication, List<PlannedDayContainer>plannedDayContainers){

        plannedDayContainers = UserAccountProvider.insertUserAccount(authentication, plannedDayContainers);

        plannedDayContainers = new ClearSavingDayContainerRequest(plannedDayContainers).getPlannedStuffContainers();
        dayContainerRepository.saveAll(plannedDayContainers);
    }

    public void save(PlannedDayContainer dayContainer){
        dayContainer = new ClearSavingDayContainerRequest(List.of(dayContainer)).getPlannedStuffContainers().get(0);
        dayContainerRepository.save(dayContainer);
    }

    public Optional<PlannedDayContainer> getPlannedDayContainerByName(String name){
        return dayContainerRepository.findByName(name);
    }

    public List<PlannedDayContainer> getDayContainers(String userEmail , Set<Long> ids) {
        return GetPlannedStuffFunc.getPlannedStuff(userEmail, ids, dayContainerRepository);
    }

    @Transactional
    public void delete(List<Long> ids) {
        ids = new ClearDeletingDayContainerRequest(ids).getIds();
        if(ids.contains(1L))
            throw new UnableToExecuteQueryException("You can not delete basic planned day container");
        dayContainerRepository.deleteAllById(ids);
    }

    @Transactional
    public void update(List<PlannedDayContainer> dayContainers) {
        dayContainers = new ClearUpdatingDayContainerRequest(dayContainers).getPlannedStuffContainers();
        for(var cont : dayContainers)
            if(cont.getId() == 1L)
                throw new UnableToExecuteQueryException("You can not update basic planned day container");
        for(var container : dayContainers){
            PlannedDayContainer plannedDayContainer = dayContainerRepository.getById(container.getId());
            String bgColor = container.getBgColor();
            if(bgColor != null)
                plannedDayContainer.setBgColor(bgColor);
            String name = container.getName();
            if(name != null)
                plannedDayContainer.setName(name);
        }
    }
}
