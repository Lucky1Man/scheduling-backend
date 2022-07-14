package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.entities.PlannedAction;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedActionRepository extends
        JpaRepository<PlannedAction, Long>,
        IPlannedStuffRepository<PlannedAction> {
//    Optional<PlannedAction> findByNameAndUserAccount_Id(String name, Long id);
//    Optional<PlannedAction> findByUserAccount_Owner_EmailAndName(String email, String name);
}
