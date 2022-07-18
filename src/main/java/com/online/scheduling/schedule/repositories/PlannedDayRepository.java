package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.interfaces.IInnerPlannedStuffRepository;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannedDayRepository extends
        JpaRepository<PlannedDay, Long>,
        IInnerPlannedStuffRepository<PlannedDay, Schedule> {

    @Override
    @Query(
            value = "select sch from schedule sch, planned_day day where day.id =?1"
    )
    List<Schedule> getPlannedStuffHolders(Long idOfStuff);

}
