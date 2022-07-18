package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedActionContainer;
import com.online.scheduling.schedule.interfaces.IInnerPlannedStuffRepository;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannedActionContainerRepository extends
        JpaRepository<PlannedActionContainer, Long>,
        IInnerPlannedStuffRepository<PlannedActionContainer, PlannedAction> {

    @Override
    @Query(
            value = "select action from planned_action action where action.plannedActionContainer.id = ?1"
    )
    List<PlannedAction> getPlannedStuffHolders(Long idOfContainer);
}
