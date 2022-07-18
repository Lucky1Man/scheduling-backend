package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.entities.PlannedDay;
import com.online.scheduling.schedule.interfaces.IInnerPlannedStuffRepository;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannedActionRepository extends
        JpaRepository<PlannedAction, Long>,
        IInnerPlannedStuffRepository<PlannedAction, PlannedDay> {
//    Optional<PlannedAction> findByNameAndUserAccount_Id(String name, Long id);
//    Optional<PlannedAction> findByUserAccount_Owner_EmailAndName(String email, String name);

    @Override
    @Query(
            value = "select day from planned_day day, planned_action action where action.id =?1"
    )
    List<PlannedDay> getPlannedStuffHolders(Long idOfStuff);
}
