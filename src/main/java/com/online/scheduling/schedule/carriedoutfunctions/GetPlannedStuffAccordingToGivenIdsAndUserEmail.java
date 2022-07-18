package com.online.scheduling.schedule.carriedoutfunctions;

import com.online.scheduling.schedule.interfaces.IPlannedStuff;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetPlannedStuffAccordingToGivenIdsAndUserEmail {
    public static <T extends IPlannedStuff<T>> List<T> getPlannedStuff(
            String userEmail,
            Set<Long> ids,
            IPlannedStuffRepository<T> repository){
        List<T> actionContainers = repository.findAllByUserAccount_Owner_Email(userEmail);
        if(ids == null || ids.isEmpty())
            return actionContainers;
        actionContainers = actionContainers.stream().filter(container -> {
            for(var id : ids){
                if(container.getId().equals(id))
                    return true;
            }
            return false;
        }).collect(Collectors.toList());
        return actionContainers;
    }
}
