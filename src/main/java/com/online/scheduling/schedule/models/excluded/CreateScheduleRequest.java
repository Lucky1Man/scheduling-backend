package com.online.scheduling.schedule.models.excluded;

import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class CreateScheduleRequest {

    //////////////current highest possible request//////////////////
    private List<PlannedDay> plannedDays;
    ////////////////////////////////////////////////////////////////


}
