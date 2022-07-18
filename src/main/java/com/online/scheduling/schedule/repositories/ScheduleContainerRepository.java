package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.Schedule;
import com.online.scheduling.schedule.entities.ScheduleContainer;
import com.online.scheduling.schedule.interfaces.IInnerPlannedStuffRepository;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleContainerRepository extends
        JpaRepository<ScheduleContainer, Long>,
        IInnerPlannedStuffRepository<ScheduleContainer, Schedule> {

    @Override
    @Query(
            value = "select sch from schedule sch where sch.scheduleContainer.id = ?1"
    )
    List<Schedule> getPlannedStuffHolders(Long idOfContainer);
}
