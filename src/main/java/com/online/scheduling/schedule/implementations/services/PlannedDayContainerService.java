package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.cleardata.planneddaycontainer.ClearDeletingDayContainerRequest;
import com.online.scheduling.schedule.cleardata.planneddaycontainer.ClearSavingDayContainerRequest;
import com.online.scheduling.schedule.cleardata.planneddaycontainer.ClearUpdatingDayContainerRequest;
import com.online.scheduling.schedule.repositories.PlannedDayContainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    public List<PlannedDayContainer> save(Authentication authentication, List<PlannedDayContainer>plannedDayContainers){
        UserAccountProvider.insertUserAccount(authentication, plannedDayContainers);

        plannedDayContainers = new ClearSavingDayContainerRequest(plannedDayContainers).getPlannedStuffContainers();
        return dayContainerRepository.saveAll(plannedDayContainers);
    }

    public void save(PlannedDayContainer dayContainer){
        dayContainer = new ClearSavingDayContainerRequest(List.of(dayContainer)).getPlannedStuffContainers().get(0);
        dayContainerRepository.save(dayContainer);
    }

    public Optional<PlannedDayContainer> getPlannedDayContainerByName(String name){
        return dayContainerRepository.findByName(name);
    }

    public List<PlannedDayContainer> get(String userEmail , Set<Long> ids) {
        return GetPlannedStuffFunc.getPlannedStuff(userEmail, ids, dayContainerRepository);
    }

    @Transactional
    public void delete(List<Long> ids) {
        ids = Set.copyOf(ids).stream().toList();
        if(ids.contains(1L))
            throw new UnableToExecuteQueryException("You can not delete basic planned day container");

        ids = new ClearDeletingDayContainerRequest(ids).getIds();

        dayContainerRepository.deleteAllById(ids);
    }

    @Transactional
    public List<PlannedDayContainer> update(List<PlannedDayContainer> dayContainers) {
        for(var cont : dayContainers)
            if(cont.getId() == 1L)
                throw new UnableToExecuteQueryException("You can not update basic planned day container");

        var result = new ArrayList<PlannedDayContainer>();
        dayContainers = new ClearUpdatingDayContainerRequest(dayContainers).getPlannedStuffContainers();
        for(var dayContainer : dayContainers){
            PlannedDayContainer currentPlannedDayContainerFromDB = dayContainerRepository.getById(dayContainer.getId());
            dayContainer.copyProperties(currentPlannedDayContainerFromDB);
            String bgColor = dayContainer.getBgColor();
            if(bgColor != null)
                currentPlannedDayContainerFromDB.setBgColor(bgColor);
            String name = dayContainer.getName();
            if(name != null)
                currentPlannedDayContainerFromDB.setName(name);
            result.add(currentPlannedDayContainerFromDB);
        }
        return result;
    }
}
