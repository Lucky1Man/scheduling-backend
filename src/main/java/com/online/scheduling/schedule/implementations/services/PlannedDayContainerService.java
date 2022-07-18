package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffAccordingToGivenIdsAndUserEmail;
import com.online.scheduling.schedule.cleardata.planneddaycontainer.ClearDeletingDayContainerRequest;
import com.online.scheduling.schedule.cleardata.planneddaycontainer.ClearSavingDayContainerRequest;
import com.online.scheduling.schedule.cleardata.planneddaycontainer.ClearUpdatingDayContainerRequest;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.enums.RequestTypes;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.repositories.PlannedDayContainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.online.scheduling.schedule.carriedoutfunctions.CheckIfContainsPlannedStuffHolders.checkIfContains;
import static com.online.scheduling.schedule.carriedoutfunctions.CheckIfUpdatingOrDeletionRequestContainsBasicPlannedStuffContainer.checkIfContainsBasicPlannedStuffContainer;
import static com.online.scheduling.schedule.carriedoutfunctions.GetAffectedPlannedStuffHolders.getAffectedPlannedStuffHolders;

@Service
public class PlannedDayContainerService {
    private final String plannedStuffName = "planned day container";
    private final PlannedDayContainerRepository dayContainerRepository;

    public PlannedDayContainerService(
            PlannedDayContainerRepository dayContainerRepository) {
        this.dayContainerRepository = dayContainerRepository;
    }

    @Transactional
    public List<PlannedDayContainer> save(Authentication authentication, List<PlannedDayContainer>plannedDayContainers){
        UserAccountProvider.insertUserAccountIfNeeded(authentication, plannedDayContainers);

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
        return GetPlannedStuffAccordingToGivenIdsAndUserEmail.getPlannedStuff(userEmail, ids, dayContainerRepository);
    }

    @Transactional
    public Map<Long, List<PlannedDay>> delete(List<Long> ids) {
        ids = Set.copyOf(ids).stream().toList();
        checkIfContainsBasicPlannedStuffContainer(ids, RequestTypes.DELETE, plannedStuffName);
        ids = new ClearDeletingDayContainerRequest(ids).getIds();

        Map<Long, List<PlannedDay>> affectedPlannedStuffHolders = getAffectedPlannedStuffHolders(ids, dayContainerRepository);
        if(!checkIfContains(ids , affectedPlannedStuffHolders))
            dayContainerRepository.deleteAllById(ids);
        return affectedPlannedStuffHolders;
    }

    @Transactional
    public List<PlannedDayContainer> update(List<PlannedDayContainer> dayContainers) {
        checkIfContainsBasicPlannedStuffContainer(RequestTypes.UPDATE, dayContainers, plannedStuffName);
        var result = new ArrayList<PlannedDayContainer>();
        dayContainers = new ClearUpdatingDayContainerRequest(dayContainers).getPlannedStuffContainers();
        for(var dayContainer : dayContainers){
            PlannedDayContainer currentPlannedDayContainerFromDB = dayContainerRepository.getById(dayContainer.getId());
            String bgColor = dayContainer.getBgColor();
            if(bgColor != null)
                currentPlannedDayContainerFromDB.setBgColor(bgColor);
            String name = dayContainer.getName();
            if(name != null)
                currentPlannedDayContainerFromDB.setName(name);
            result.add(currentPlannedDayContainerFromDB);
            dayContainer.copyProperties(currentPlannedDayContainerFromDB);
        }
        return result;
    }
}
