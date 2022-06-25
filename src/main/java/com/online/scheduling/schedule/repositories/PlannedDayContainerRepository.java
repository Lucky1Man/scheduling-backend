package com.online.scheduling.schedule.repositories;

import com.online.scheduling.schedule.entities.PlannedDayContainer;
import com.online.scheduling.schedule.interfaces.IPlannedStuffRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedDayContainerRepository extends
        JpaRepository<PlannedDayContainer, Long>,
        IPlannedStuffRepository<PlannedDayContainer> {
}
