package com.online.scheduling.schedule.implementations.services.excluded;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.implementations.services.PlannedActionContainerService;
import com.online.scheduling.schedule.implementations.services.PlannedActionService;
import com.online.scheduling.schedule.implementations.services.PlannedDayContainerService;
import com.online.scheduling.schedule.implementations.services.PlannedDayService;
import com.online.scheduling.schedule.models.ClearSavingDaysRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("SchedulingManagementService")
public class ScheduleManagementService {
    private final PlannedActionService actionService;
    private final PlannedDayService dayService;
    private final PlannedActionContainerService actionContainerService;
    private final PlannedDayContainerService dayContainerService;

    public ScheduleManagementService(
            PlannedActionService actionService,
            PlannedDayService dayService,
            PlannedActionContainerService actionContainerService,
            PlannedDayContainerService dayContainerService) {
        this.actionService = actionService;
        this.dayService = dayService;
        this.actionContainerService = actionContainerService;
        this.dayContainerService = dayContainerService;
    }



    @Transactional
    public void saveSchedule(ClearSavingDaysRequest requestHolder) throws RuntimeException{

        //saving containers
        if(requestHolder.areThereAnyAblePlannedActionContainers())
            actionContainerService.saveAll(requestHolder.getPlannedActionContainers());
        if(requestHolder.areThereAnyAblePlannedDayContainers())
            dayContainerService.saveAll(requestHolder.getPlannedDayContainers());

        //saving planned stuff
        actionService.saveAll(requestHolder.getPlannedActions());
        dayService.saveAll(requestHolder.getPlannedDays());
    }

//    public List<PlannedDay> getMatchingDays(List<Long> ids) {
//        if(ids == null || ids.isEmpty())
//            return dayService.getAllDays();
//        return dayService.getAllDaysByIds(ids);
//    }

//    public List<PlannedAction> getMatchingActions(List<Long> ids) {
//        if(ids == null || ids.isEmpty())
//            return actionService.getAllDays();
//        return actionService.getAllDaysByIds(ids);
//    }

//    public List<PlannedActionContainer> getMatchingActionContainers(List<Long> ids){
//        if(ids == null || ids.isEmpty())
//            return actionContainerService.getAllActionContainers();
//        return actionContainerService.getAllActionContainersByIds(ids);
//    }

//    public List<PlannedDayContainer> getMatchingDayContainers(List<Long> ids) {
//        if(ids == null || ids.isEmpty())
//            return dayContainerService.getAllDayContainers();
//        return dayContainerService.getAllDayContainersById(ids);
//    }
}
