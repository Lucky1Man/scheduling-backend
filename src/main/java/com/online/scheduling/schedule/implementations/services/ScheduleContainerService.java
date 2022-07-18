package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffAccordingToGivenIdsAndUserEmail;
import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.enums.RequestTypes;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.cleardata.shcedulecontainer.ClearDeletingScheduleContainerRequest;
import com.online.scheduling.schedule.cleardata.shcedulecontainer.ClearSavingScheduleContainerRequest;
import com.online.scheduling.schedule.cleardata.shcedulecontainer.ClearUpdatingScheduleContainerRequest;
import com.online.scheduling.schedule.repositories.ScheduleContainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.online.scheduling.schedule.carriedoutfunctions.CheckIfContainsPlannedStuffHolders.checkIfContains;
import static com.online.scheduling.schedule.carriedoutfunctions.CheckIfUpdatingOrDeletionRequestContainsBasicPlannedStuffContainer.checkIfContainsBasicPlannedStuffContainer;
import static com.online.scheduling.schedule.carriedoutfunctions.GetAffectedPlannedStuffHolders.getAffectedPlannedStuffHolders;

@Service
public class ScheduleContainerService {
    private final String plannedStuffName = "schedule container";
    private final ScheduleContainerRepository scheduleContainerRepository;

    public ScheduleContainerService(
            ScheduleContainerRepository scheduleContainerRepository) {
        this.scheduleContainerRepository = scheduleContainerRepository;
    }

    @Transactional
    public List<ScheduleContainer> save(Authentication authentication, List<ScheduleContainer> scheduleContainers) {
        UserAccountProvider.insertUserAccountIfNeeded(authentication, scheduleContainers);

        scheduleContainers = new ClearSavingScheduleContainerRequest(scheduleContainers).getPlannedStuffContainers();
        return scheduleContainerRepository.saveAll(scheduleContainers);
    }

    @Transactional
    public void save(ScheduleContainer scheduleContainer){
        scheduleContainer = new ClearSavingScheduleContainerRequest(List.of(scheduleContainer)).getPlannedStuffContainers().get(0);
        scheduleContainerRepository.save(scheduleContainer);
    }
    public Optional<ScheduleContainer> getPlannedDayContainerByName(String name){
        return scheduleContainerRepository.findByName(name);
    }

    public List<ScheduleContainer> get(String userEmail , Set<Long> ids) {
        return GetPlannedStuffAccordingToGivenIdsAndUserEmail.getPlannedStuff(userEmail,ids,scheduleContainerRepository);
    }

    @Transactional
    public Map<Long, List<Schedule>> delete(List<Long> ids) {
        ids = Set.copyOf(ids).stream().toList();
        checkIfContainsBasicPlannedStuffContainer(ids, RequestTypes.DELETE, plannedStuffName);
        ids = new ClearDeletingScheduleContainerRequest(ids).getIds();

        Map<Long, List<Schedule>> affectedPlannedStuffHolders = getAffectedPlannedStuffHolders(ids, scheduleContainerRepository);
        if(!checkIfContains(ids , affectedPlannedStuffHolders))
            scheduleContainerRepository.deleteAllById(ids);
        return affectedPlannedStuffHolders;
    }

    @Transactional
    public List<ScheduleContainer> update(List<ScheduleContainer> scheduleContainers) {
        checkIfContainsBasicPlannedStuffContainer(RequestTypes.UPDATE, scheduleContainers, plannedStuffName);
        var result = new ArrayList<ScheduleContainer>();
        scheduleContainers = new ClearUpdatingScheduleContainerRequest(scheduleContainers).getPlannedStuffContainers();
        for(var container : scheduleContainers){
            var currentScheduleContainerFromDB = scheduleContainerRepository.getById(container.getId());
            String name = container.getName();
            if(name != null)
                currentScheduleContainerFromDB.setName(name);
            String bgColor = container.getBgColor();
            if(bgColor != null)
                currentScheduleContainerFromDB.setBgColor(bgColor);
            result.add(currentScheduleContainerFromDB);
            container.copyProperties(currentScheduleContainerFromDB);
        }
        return result;
    }
}
