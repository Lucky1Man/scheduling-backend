package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.interfaces.IInnerPlannedStuffRepository;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannedDayContainerRepository extends
        JpaRepository<PlannedDayContainer, Long>,
        IInnerPlannedStuffRepository<PlannedDayContainer, PlannedDay> {
    @Override
    @Query(
            value = "select day from planned_day day where day.plannedDayContainer.id = ?1"
    )
    List<PlannedDay> getPlannedStuffHolders(Long idOfContainer);
}
