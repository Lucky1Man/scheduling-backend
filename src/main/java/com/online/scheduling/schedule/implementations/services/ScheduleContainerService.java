package com.online.scheduling.schedule.implementations.services;

import com.online.scheduling.exceptions.UnableToExecuteQueryException;
import com.online.scheduling.schedule.carriedoutfunctions.GetPlannedStuffFunc;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.implementations.providers.UserAccountProvider;
import com.online.scheduling.schedule.cleardata.shcedulecontainer.ClearDeletingScheduleContainerRequest;
import com.online.scheduling.schedule.cleardata.shcedulecontainer.ClearSavingScheduleContainerRequest;
import com.online.scheduling.schedule.cleardata.shcedulecontainer.ClearUpdatingScheduleContainerRequest;
import com.online.scheduling.schedule.repositories.ScheduleContainerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ScheduleContainerService {
    private final ScheduleContainerRepository scheduleContainerRepository;

    public ScheduleContainerService(
            ScheduleContainerRepository scheduleContainerRepository) {
        this.scheduleContainerRepository = scheduleContainerRepository;
    }

    @Transactional
    public List<ScheduleContainer> save(Authentication authentication, List<ScheduleContainer> scheduleContainers) {
        UserAccountProvider.insertUserAccount(authentication, scheduleContainers);

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
        return GetPlannedStuffFunc.getPlannedStuff(userEmail,ids,scheduleContainerRepository);
    }

    @Transactional
    public void delete(List<Long> ids) {
        if(ids.contains(1L))
            throw new UnableToExecuteQueryException("You can not delete basic schedule container");
        ids = new ClearDeletingScheduleContainerRequest(ids).getIds();
        scheduleContainerRepository.deleteAllById(ids);
    }

    @Transactional
    public List<ScheduleContainer> update(List<ScheduleContainer> scheduleContainers) {
        var result = new ArrayList<ScheduleContainer>();
        for(var cont : scheduleContainers)
            if(cont.getId() == 1L)
                throw new UnableToExecuteQueryException("You can not update basic schedule container");
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
